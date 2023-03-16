package com.shopee.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registration_user_token")
public class RegistrationUserTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    private String token;

    @OneToOne(targetEntity = UserShopEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserShopEntity user;

    public RegistrationUserTokenEntity(UserShopEntity user, String token) {
        this.user = user;
        this.token = token;
    }
}
