package com.shopee.repositories;

import com.shopee.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long>, JpaSpecificationExecutor<OrderItemEntity> {
    @Transactional
    @Modifying
    @Query(value = "delete from OrderItemEntity where id = :orderItemId")
    void deleteOrderItem(Long orderItemId);
}
