package com.shopee.controller;

import com.shopee.config.Config;
import com.shopee.entity.UserShopEntity;
import com.shopee.request.VnPayRequest;
import com.shopee.response.VnPayResponse;
import com.shopee.service.SaleOrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/v1/payment")
@SecurityRequirement(name = "Authorization")
public class PaymentController {
    private SaleOrderService saleOrderService;

    @Value("${vnPay.tmnCode}")
    private String tmnCode;

    @Value("${vnPay.hashSecret}")
    private String hashSecret;

    @Value("${vnPay.url}")
    private String url;

    @PostMapping
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    public ResponseEntity<?> createPayment(@RequestBody VnPayRequest vnPayRequest, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", "VUXA6Y81");
        params.put("vnp_Amount", "1000000");
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", "5");
        params.put("vnp_OrderInfo", "Thanh toan don hang");
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", "http://localhost:8080/api/v1/products");
        params.put("vnp_IpAddr", "183.81.123.182");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
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
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        VnPayResponse vnPayResponse = new VnPayResponse(
                "00", "success", paymentUrl
        );

        return ResponseEntity.status(HttpStatus.OK).body(vnPayResponse);
    }

}
