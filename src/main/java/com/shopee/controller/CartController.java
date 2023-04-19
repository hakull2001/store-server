package com.shopee.controller;

import com.shopee.dto.OderItemDTO;
import com.shopee.dto.base.BaseController;
import com.shopee.entity.*;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.NotFoundException;
import com.shopee.service.DeliveryService;
import com.shopee.service.OrderItemService;
import com.shopee.service.ProductService;
import com.shopee.service.SaleOrderService;
import com.shopee.specification.GenericSpecification;
import com.shopee.specification.JoinCriteria;
import com.shopee.specification.SearchCriteria;
import com.shopee.specification.SearchOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping(value = "/api/v1/carts")
@SecurityRequirement(name = "Authorization")
public class CartController extends BaseController<Object> {

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    public ResponseEntity<?> getOrderItemsOfCart(HttpServletRequest request) {

        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");

        DeliveryEntity delivery = deliveryService.findByAddedToCartState();
        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<>();
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "user", "user_id",
                requestedUser.getUserId(), JoinType.INNER));
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "delivery", "id",
                delivery.getId(), JoinType.INNER));

        SaleOrderEntity saleOrder = saleOrderService.findOne(specification);

        return this.resSuccess(saleOrder);
    }

    @GetMapping("/amount")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    public Long getAmountItemCart(HttpServletRequest request) {
        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");

        DeliveryEntity delivery = deliveryService.findByAddedToCartState();
        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<>();
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "user", "user_id",
                requestedUser.getUserId(), JoinType.INNER));
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "delivery", "id",
                delivery.getId(), JoinType.INNER));

        SaleOrderEntity saleOrder = saleOrderService.findOne(specification);
        AtomicReference<Long> amount = new AtomicReference<>(0L);
        saleOrder.getOrderItems().forEach(e -> {
            amount.updateAndGet(v -> v + e.getQuantity());
        });
        return amount.get();
    }
    @GetMapping("/subtotal")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    public Long getSubtotal(HttpServletRequest request) {
        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");

        DeliveryEntity delivery = deliveryService.findByAddedToCartState();
        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<>();
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "user", "user_id",
                requestedUser.getUserId(), JoinType.INNER));
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "delivery", "id",
                delivery.getId(), JoinType.INNER));

        SaleOrderEntity saleOrder = saleOrderService.findOne(specification);
        AtomicReference<Long> amount = new AtomicReference<>(0L);
        saleOrder.getOrderItems().forEach(e -> {
            amount.updateAndGet(v -> v + e.getQuantity() * e.getProduct().getPrice());
        });
        return amount.get();
    }
    @PostMapping
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> addToCart(@RequestBody @Valid OderItemDTO orderItemDTO, HttpServletRequest request) {
        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");

        ProductEntity product = productService.getDetailProduct(orderItemDTO.getProductId());
        if (product == null) {
            throw new NotFoundException("Not found product");
        }

        if (product.getAmount() < orderItemDTO.getQuantity()) {
            throw new AppException("Not enough quantity");
        }

        DeliveryEntity delivery = deliveryService.findByAddedToCartState();

        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<>();
        specification.add(new SearchCriteria("user", requestedUser.getUserId(), SearchOperation.EQUAL));
        specification.add(new SearchCriteria("delivery", delivery.getId(), SearchOperation.EQUAL));
        SaleOrderEntity oldSaleOrder = saleOrderService.findOne(specification);

        // update order item if exists
        if (oldSaleOrder != null) {
            GenericSpecification<OrderItemEntity> orderItemGenericSpecification = new GenericSpecification<>();
            orderItemGenericSpecification.add(new SearchCriteria("saleOrder", oldSaleOrder.getId(), SearchOperation.EQUAL));
            orderItemGenericSpecification.add(new SearchCriteria("product", product.getId(), SearchOperation.EQUAL));

            OrderItemEntity oldOrderItem = orderItemService.findOne(orderItemGenericSpecification);

            OrderItemEntity newOrderItem;

            if (oldOrderItem != null) {
                oldOrderItem.setQuantity(oldOrderItem.getQuantity() + orderItemDTO.getQuantity());
                oldOrderItem.setColor(orderItemDTO.getColor());
                newOrderItem = orderItemService.createOrUpdate(oldOrderItem);
            } else {
                OrderItemEntity orderItem = new OrderItemEntity();
                orderItem.setSaleOrder(oldSaleOrder);
                orderItem.setProduct(product);
                orderItem.setQuantity(orderItemDTO.getQuantity());
                orderItem.setColor(orderItemDTO.getColor());
                newOrderItem = orderItemService.createOrUpdate(orderItem);
            }

            return this.resSuccess(newOrderItem);
        }

        // create new sale order and order item
        SaleOrderEntity saleOrder = new SaleOrderEntity();
        saleOrder.setUser(requestedUser);
        saleOrder.setDelivery(delivery);
        saleOrder.setCustomerAddress(requestedUser.getAddress());
        saleOrder.setPhoneNumber(requestedUser.getPhoneNumber());

        SaleOrderEntity newSaleOrder = saleOrderService.create(saleOrder);

        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setSaleOrder(newSaleOrder);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setColor(orderItemDTO.getColor());
        OrderItemEntity newOrderItem = orderItemService.createOrUpdate(orderItem);

        return this.resSuccess(newOrderItem);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deleteByProductId(@PathVariable Long id){
        OrderItemEntity orderItem = orderItemService.findById(id);
        orderItemService.deleteById(orderItem.getId());

        return this.resSuccess("success");
    }

}