package com.shopee.service;

import com.shopee.request.user.LoginRequest;

public interface AuthService {
    void login(LoginRequest request);
}
