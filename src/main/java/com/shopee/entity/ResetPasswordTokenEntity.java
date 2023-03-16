package com.shopee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reset_password_token")
@Entity
public class ResetPasswordTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resetPasswordId;

    private String token;

    @OneToOne(targetEntity = UserShopEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserShopEntity user;

    public ResetPasswordTokenEntity(UserShopEntity user, String token) {
        this.user = user;
        this.token = token;
    }
}
