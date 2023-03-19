package com.shopee.service;

import com.shopee.dto.PaginateDTO;
import com.shopee.entity.OrderItemEntity;
import com.shopee.entity.SaleOrderEntity;
import com.shopee.specification.GenericSpecification;

import java.util.List;

public interface SaleOrderService {
    SaleOrderEntity findOne(GenericSpecification<SaleOrderEntity> specification);

    SaleOrderEntity findById(Long saleOrderId);

    SaleOrderEntity create(SaleOrderEntity saleOrder);

    SaleOrderEntity update(SaleOrderEntity saleOrder);

    void deleteById(Long saleOrderId);

    Long calculateTotalAmount(List<OrderItemEntity> orderItems);

    PaginateDTO<SaleOrderEntity> getList(Integer page, Integer perPage, GenericSpecification<SaleOrderEntity> specification);
}
