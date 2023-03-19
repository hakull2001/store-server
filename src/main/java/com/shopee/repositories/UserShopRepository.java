package com.shopee.repositories;

import com.shopee.entity.UserShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserShopRepository extends JpaRepository<UserShopEntity, Long> {
    UserShopEntity findByUsername(String username);

    UserShopEntity findByEmail(String email);

    UserShopEntity findByUserId(Long userId);

    Optional<UserShopEntity> findByEmailOrUsernameOrPhoneNumber(String email, String username, String phoneNumber);
}
