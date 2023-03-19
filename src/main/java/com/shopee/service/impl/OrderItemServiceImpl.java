package com.shopee.service.impl;

import com.shopee.dto.base.BasePagination;
import com.shopee.entity.OrderItemEntity;
import com.shopee.repositories.OrderItemRepository;
import com.shopee.service.OrderItemService;
import com.shopee.specification.GenericSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends BasePagination<OrderItemEntity, OrderItemRepository> implements OrderItemService {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderItemEntity findById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElse(null);
    }

    @Override
    public OrderItemEntity findOne(GenericSpecification<OrderItemEntity> specification) {
        return orderItemRepository.findOne(specification).orElse(null);
    }

    @Override
    public OrderItemEntity createOrUpdate(OrderItemEntity orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public void deleteById(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }
}
