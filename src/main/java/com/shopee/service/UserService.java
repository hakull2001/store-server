package com.shopee.service;

import com.shopee.entity.UserShopEntity;
import com.shopee.request.user.SignUpRequest;

import java.util.List;

public interface UserService {
    List<UserShopEntity> getAllUsers();

    UserShopEntity findByUsername(String username);

    UserShopEntity signUp(SignUpRequest request) throws Exception;

    void activeUser(String token);

    UserShopEntity findByEmail(String email);

    UserShopEntity findByUserId(Long userId);

    void updateUser(UserShopEntity user);

}
