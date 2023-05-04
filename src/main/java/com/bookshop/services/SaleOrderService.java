package com.bookshop.services;

import com.bookshop.dao.OrderItem;
import com.bookshop.dao.SaleOrder;
import com.bookshop.dto.DasboardDTO;
import com.bookshop.dto.pagination.PaginateDTO;
import com.bookshop.specifications.GenericSpecification;

import java.util.List;

public interface SaleOrderService {
    SaleOrder findOne(GenericSpecification<SaleOrder> specification);

    SaleOrder findById(Long saleOrderId);

    SaleOrder create(SaleOrder saleOrder);

    SaleOrder update(SaleOrder saleOrder);

    void deleteById(Long saleOrderId);

    Long calculateTotalAmount(List<OrderItem> orderItems);

    List<SaleOrder> getALl();

    List<DasboardDTO> getAllMonths();

    PaginateDTO<SaleOrder> getList(Integer page, Integer perPage, GenericSpecification<SaleOrder> specification);

    List<SaleOrder> getList();
}
