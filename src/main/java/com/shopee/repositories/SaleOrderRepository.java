package com.shopee.repositories;

import com.shopee.entity.SaleOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrderEntity, Long>, JpaSpecificationExecutor<SaleOrderEntity> {
}
