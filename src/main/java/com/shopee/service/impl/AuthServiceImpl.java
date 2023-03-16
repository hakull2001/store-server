package com.shopee.service.impl;

import com.shopee.entity.UserShopEntity;
import com.shopee.enumerations.UserStatus;
import com.shopee.exceptions.AppException;
import com.shopee.repositories.UserRepository;
import com.shopee.request.user.LoginRequest;
import com.shopee.response.ResultResponse;
import com.shopee.response.UserInformationResponse;
import com.shopee.service.AuthService;
import com.shopee.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResultResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AppException("Incorrect username or password");
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        UserShopEntity user = userRepository.findByUsername(request.getUsername());
        if (user.getStatus().equals(UserStatus.NOT_ACTIVE))
            throw new AppException("Please check your email to active user");
        user.setLastLogin(Instant.now());
        userRepository.save(user);
        return ResultResponse.SUCCESS.withResult(new UserInformationResponse(token, user));
    }
}
