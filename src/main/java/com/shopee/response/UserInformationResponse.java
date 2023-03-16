package com.shopee.response;

import com.shopee.entity.UserShopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationResponse {
    private String token;

    private UserShopEntity user;

}
