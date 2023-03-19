package com.shopee.controller;

import com.shopee.dto.base.BaseController;
import com.shopee.entity.ResetPasswordTokenEntity;
import com.shopee.entity.UserShopEntity;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.NotFoundException;
import com.shopee.request.email.SendEmailRequest;
import com.shopee.request.user.ForgotPasswordRequest;
import com.shopee.request.user.LoginRequest;
import com.shopee.request.user.ResetPasswordRequest;
import com.shopee.request.user.SignUpRequest;
import com.shopee.response.AuthenticationResponse;
import com.shopee.response.ResultResponse;
import com.shopee.service.*;
import com.shopee.service.impl.MyUserDetailsServiceImpl;
import com.shopee.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController<Object> {
    private static final String MSG_SUBJECT_MAIL = "msg.subject.mail";
    private static final String MSG_CONTENT_MAIL = "msg.content.mail";
    private static final String MSC_SIGN_UP_SUCCESS = "msg.signup.success";
    private static final String MSG_LOGIN_FAIL = "msg.login.fail";
    private static final String MSG_NOT_FOUND_USER = "msg.not.found.user";
    private static final String MSG_SUBJECT_FORGOT_PASSWORD = "msg.subject.forgot.password";

    private static final String MSG_CONTENT_FORGOT_PASSWORD = "msg.content.forgot.password";

    private static final String MSG_FORGOT_PASSWORD_SUCCESS = "msg.forgot.password.success";

    private static final String MSG_RESET_PASSWORD_SUCCESS = "msg.reset.password.success";

    private static final String MSG_ACTIVE_USER_SUCCESS = "msg.active.user.success";
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationUserToken registrationUserToken;

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) throws Exception {
        UserShopEntity user = userService.signUp(request);
        final String token = jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(user.getUsername()));
        registrationUserToken.createNewRegistrationUserToken(user,token);
        emailService.send(new SendEmailRequest(
                request.getEmail(),
                MSG_CONTENT_MAIL + token,
                MSG_SUBJECT_MAIL
        ), true);
        return this.resSuccess(new AuthenticationResponse(token, user));
    }

    @PostMapping("/active/{token}")
    public ResponseEntity<?> activeUser(@PathVariable(value = "token") String token){
        userService.activeUser(token);
        return new ResponseEntity<>(MSG_ACTIVE_USER_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request){
        AuthenticationResponse response =(AuthenticationResponse) authService.login(request).getResult();
        return this.resSuccess(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) throws MessagingException {
        UserShopEntity user = userService.findByEmail(request.getEmail());
        if(user == null)
            throw new NotFoundException(MSG_NOT_FOUND_USER);

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        resetPasswordTokenService.createTokenToResetPassword(
                new ResetPasswordTokenEntity(user, token)
        );

        emailService.send(new SendEmailRequest(request.getEmail(),
                MSG_CONTENT_FORGOT_PASSWORD + token,
                MSG_SUBJECT_FORGOT_PASSWORD),
                true);

        return new ResponseEntity<>(MSG_FORGOT_PASSWORD_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable(value = "token") String token, @RequestBody @Valid ResetPasswordRequest request){
        ResetPasswordTokenEntity resetPasswordTokenEntity = resetPasswordTokenService.findByToken(token);
        if(!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new AppException("Password must match");

        UserShopEntity user = userService.findByUserId(resetPasswordTokenEntity.getUser().getUserId());
        user.setPassword(request.getNewPassword());
        userService.updateUser(user);

        resetPasswordTokenService.deleteByResetPasswordId(resetPasswordTokenEntity.getResetPasswordId());
        return new ResponseEntity<>(MSG_RESET_PASSWORD_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody AuthenticationResponse authenticationResponse) {
        try {
            String jwt = authenticationResponse.getJwt();
            String username = jwtUtil.extractUsername(jwt);
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            UserShopEntity user = userService.findByUsername(username);
            return this.resSuccess(new AuthenticationResponse(jwtUtil.generateToken(userDetails), user));
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }
}
