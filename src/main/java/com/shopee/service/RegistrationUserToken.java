package com.shopee.service;

import com.shopee.entity.RegistrationUserTokenEntity;
import com.shopee.entity.UserShopEntity;

public interface RegistrationUserToken {
    void createNewRegistrationUserToken(UserShopEntity user, final String token);

    RegistrationUserTokenEntity findByToken(String token);
}
