package com.shopee.controller;

import com.shopee.dto.OrderItemUpdateDTO;
import com.shopee.dto.base.BaseController;
import com.shopee.entity.OrderItemEntity;
import com.shopee.entity.ProductEntity;
import com.shopee.entity.SaleOrderEntity;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.NotFoundException;
import com.shopee.service.OrderItemService;
import com.shopee.service.ProductService;
import com.shopee.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/order-items")
public class OrderItemController extends BaseController<OrderItemEntity> {
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleOrderService saleOrderService;

    @PatchMapping("/{orderItemId}")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateQuantity(@PathVariable("orderItemId") Long orderItemId,
                                            @RequestBody @Valid OrderItemUpdateDTO orderItemUpdateDTO) {
        OrderItemEntity orderItem = orderItemService.findById(orderItemId);
        if (orderItem == null) {
            throw new NotFoundException("Not found order item");
        }

        ProductEntity product = orderItem.getProduct();

        Long currentNumber = product.getAmount() + orderItem.getQuantity();
        if (currentNumber < orderItemUpdateDTO.getQuantity()) {
            throw new AppException("Not enough quantity");
        }

        Long updatedCurrentNumber = (product.getAmount() + orderItem.getQuantity()) - orderItemUpdateDTO.getQuantity();

        orderItem.setQuantity(orderItemUpdateDTO.getQuantity());
        orderItemService.createOrUpdate(orderItem);

        product.setAmount(updatedCurrentNumber);
        productService.update(product);

        return this.resSuccess(orderItem);
    }

    @DeleteMapping("/{orderItemId}")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deleteOrderItem(@PathVariable("orderItemId") Long orderItemId) {
        OrderItemEntity orderItem = orderItemService.findById(orderItemId);
        if (orderItem == null) {
            throw new NotFoundException("Not found order item");
        }

        orderItemService.deleteById(orderItemId);

        SaleOrderEntity saleOrder = saleOrderService.findById(orderItem.getSaleOrder().getId());
        if (saleOrder.getOrderItems().size() == 0) {
            saleOrderService.deleteById(saleOrder.getId());
        }

        return this.resSuccess(orderItem);
    }
}
