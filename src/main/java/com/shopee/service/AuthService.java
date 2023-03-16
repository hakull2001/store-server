package com.shopee.service;

import com.shopee.request.user.LoginRequest;
import com.shopee.response.ResultResponse;

public interface AuthService {
    ResultResponse login(LoginRequest request);
}
