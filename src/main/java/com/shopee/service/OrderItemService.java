package com.shopee.service;

import com.shopee.entity.OrderItemEntity;
import com.shopee.specification.GenericSpecification;

public interface OrderItemService {
    OrderItemEntity findById(Long orderItemId);

    OrderItemEntity findOne(GenericSpecification<OrderItemEntity> specification);

    OrderItemEntity createOrUpdate(OrderItemEntity orderItem);

    void deleteById(Long orderItemId);
}
