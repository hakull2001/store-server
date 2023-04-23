package com.bookshop.controllers;

import com.bookshop.base.BaseController;
import com.bookshop.configs.VnPayConfig;
import com.bookshop.dao.PaymentUser;
import com.bookshop.dao.User;
import com.bookshop.dto.UserChangePasswordDTO;
import com.bookshop.dto.UserDTO;
import com.bookshop.dto.UserUpdateDTO;
import com.bookshop.dto.pagination.PaginateDTO;
import com.bookshop.exceptions.AppException;
import com.bookshop.exceptions.NotFoundException;
import com.bookshop.request.VnpayRequest;
import com.bookshop.services.PaymentUserService;
import com.bookshop.services.UserService;
import com.bookshop.specifications.GenericSpecification;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/users")
@SecurityRequirement(name = "Authorization")
public class UserController extends BaseController<User> {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentUserService paymentUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/check")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    public ResponseEntity<?> checkPayment(HttpServletRequest request) {
        User requestedUser = (User) request.getAttribute("user");
        try {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            String signValue = VnPayConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                PaymentUser check = paymentUserService.findByVnpTxnRef(request.getParameter("vnp_TxnRef"));
                if (check == null)
                    throw new NotFoundException("Đơn giao dịch này không tồn tại");
                else {
                    if (!request.getParameter("vnp_Amount").equals(String.valueOf(Long.valueOf(check.getVnpAmount()) * 100))) {
                        throw new NotFoundException("Số tiền không đúng");
                    } else {
                        if (check.getPaymentStatus().equals(0L)) {
                            if (request.getParameter("vnp_ResponseCode").equals("00"))
                                check.setPaymentStatus(1L);
                            else
                                check.setPaymentStatus(2L);
                        } else
                            throw new NotFoundException("Giao dịch này đã được xác nhận");
                    }
                }
                check.setVnpPayDate(request.getParameter("vnp_PayDate"));
                check.setVnpBankCode(request.getParameter("vnp_BankCode"));

                paymentUserService.update(check);
                requestedUser.setAmount(requestedUser.getAmount() + Long.valueOf(check.getVnpAmount()));
                userService.update(requestedUser);
                return this.resSuccess(requestedUser);
            } else {
                throw new NotFoundException("Không hợp lệ đâu");
            }
        } catch (Exception e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @PostMapping("/payment")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    public ResponseEntity<?> payment(@RequestBody VnpayRequest vnpayRequest, HttpServletRequest request) throws UnsupportedEncodingException {
        User requestedUser = (User) request.getAttribute("user");
        PaymentUser paymentUser = new PaymentUser();
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", "VUXA6Y81");
        params.put("vnp_Amount", String.valueOf(Long.valueOf(vnpayRequest.getVnpAmount()) * 100));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", VnPayConfig.getRandomNumber(8));
        params.put("vnp_OrderInfo", "naptien " + requestedUser.getUsername());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", "http://localhost:3000/profile/recharge");
        params.put("vnp_IpAddr", "183.81.123.182");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat result = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String test = result.format(cld.getTime());
        String vnp_CreateDate = formatter.format(cld.getTime());
        params.put("vnp_CreateDate", vnp_CreateDate);

        List fieldNames = new ArrayList(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;
        paymentUser.setVnpTxnref(params.get("vnp_TxnRef"));
        paymentUser.setUser(requestedUser);
        paymentUser.setVnpAmount(vnpayRequest.getVnpAmount());
        paymentUser.setVnpPayDate(test);
        paymentUser.setVnpOrderInfo(params.get("vnp_OrderInfo"));
        paymentUser.setPaymentStatus(0L);
        paymentUserService.create(paymentUser);
        return new ResponseEntity<>(paymentUrl, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("@userAuthorizer.isAdmin(authentication)")
    public ResponseEntity<?> getListUsers(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "perPage", required = false) Integer perPage,
            HttpServletRequest request) {

        GenericSpecification<User> specification = new GenericSpecification<User>().getBasicQuery(request);

        PaginateDTO<User> paginateUsers = userService.getList(page, perPage, specification);

        return this.resPagination(paginateUsers);
    }

    @PostMapping
    @PreAuthorize("@userAuthorizer.isAdmin(authentication)")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDTO) {
        User oldUser = userService.findByUsername(userDTO.getUsername());

        if (oldUser != null) {
            throw new AppException("Tài khoản đã tồn tại");
        }

        User user = userService.create(userDTO);
        return this.resSuccess(user);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@userAuthorizer.isAdmin(authentication) || @userAuthorizer.isYourself(authentication, #userId)")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);

        return this.resSuccess(user);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("@userAuthorizer.isAdmin(authentication) || @userAuthorizer.isYourself(authentication, #userId)")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateDTO userUpdateDTO, @PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new NotFoundException("Không tìm thấy người dùng này");
        }

        User savedUser = userService.update(userUpdateDTO, user);

        return this.resSuccess(savedUser);
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid UserChangePasswordDTO userChangePasswordDTO, HttpServletRequest request) {
        User requestedUser = (User) request.getAttribute("user");

        User user = userService.findByUsername(requestedUser.getUsername());

        if (!passwordEncoder.matches(userChangePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new AppException("Mật khẩu cũ không chính xác");
        }

        user.setPassword(passwordEncoder.encode(userChangePasswordDTO.getNewPassword()));

        User updatedUser = userService.update(user);

        return this.resSuccess(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@userAuthorizer.isAdmin(authentication)")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new NotFoundException("Không tìm thấy người dùng này");
        }

        if (!user.getSaleOrders().isEmpty()) {
            throw new AppException("Không thể xóa người dùng này");
        }

        userService.deleteById(userId);

        return this.resSuccess(user);
    }
}
