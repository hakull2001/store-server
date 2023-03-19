package com.shopee.repositories;

import com.shopee.entity.ProductRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRateRepository extends JpaRepository<ProductRateEntity, Long> {
}
