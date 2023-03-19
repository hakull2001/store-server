package com.shopee.service.impl;

import com.shopee.dto.PaginateDTO;
import com.shopee.dto.base.BasePagination;
import com.shopee.entity.OrderItemEntity;
import com.shopee.entity.SaleOrderEntity;
import com.shopee.repositories.SaleOrderRepository;
import com.shopee.service.SaleOrderService;
import com.shopee.specification.GenericSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleOrderServiceImpl extends BasePagination<SaleOrderEntity, SaleOrderRepository> implements SaleOrderService {
    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    public SaleOrderServiceImpl(SaleOrderRepository saleOrderRepository) {
        super(saleOrderRepository);
    }

    @Override
    public SaleOrderEntity findOne(GenericSpecification<SaleOrderEntity> specification) {
        return saleOrderRepository.findOne(specification).orElse(null);
    }

    @Override
    public SaleOrderEntity findById(Long saleOrderId) {
        return saleOrderRepository.findById(saleOrderId).orElse(null);
    }

    @Override
    public SaleOrderEntity create(SaleOrderEntity saleOrder) {
        return saleOrderRepository.save(saleOrder);
    }

    @Override
    public SaleOrderEntity update(SaleOrderEntity saleOrder) {
        return saleOrderRepository.save(saleOrder);
    }

    @Override
    public void deleteById(Long saleOrderId) {
        saleOrderRepository.deleteById(saleOrderId);
    }

    @Override
    public Long calculateTotalAmount(List<OrderItemEntity> orderItems) {
        long totalAmount = 0L;
        for (OrderItemEntity orderItem : orderItems) {
            totalAmount += orderItem.getProduct().getPrice() * orderItem.getQuantity();
        }
        return totalAmount;
    }

    @Override
    public PaginateDTO<SaleOrderEntity> getList(Integer page, Integer perPage, GenericSpecification<SaleOrderEntity> specification) {
        return this.paginate(page, perPage, specification);
    }
}
