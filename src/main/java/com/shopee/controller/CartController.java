package com.shopee.controller;

import com.shopee.dto.base.BaseController;
import com.shopee.entity.DeliveryEntity;
import com.shopee.entity.SaleOrderEntity;
import com.shopee.entity.UserShopEntity;
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
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
        System.out.println(requestedUser.getUserId());

        DeliveryEntity delivery = deliveryService.findByAddedToCartState();

        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<>();
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "user", "user_id",
                requestedUser.getUserId(), JoinType.INNER));
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "delivery", "id",
                delivery.getId(), JoinType.INNER));

        SaleOrderEntity saleOrder = saleOrderService.findOne(specification);

        return this.resSuccess(saleOrder);
    }

//    @PostMapping
//    @PreAuthorize("@userAuthorizer.isMember(authentication)")
//    @Transactional(rollbackFor = Exception.class)
//    public ResponseEntity<?> addToCart(@RequestBody @Valid OrderItemDTO orderItemDTO, HttpServletRequest request) {
//        User requestedUser = (User) request.getAttribute("user");
//
//        Product product = productService.findById(orderItemDTO.getProductId());
//        if (product == null) {
//            throw new NotFoundException("Not found product");
//        }
//
//        if (product.getCurrentNumber() < orderItemDTO.getQuantity()) {
//            throw new AppException("Not enough quantity");
//        }
//
//        Delivery delivery = deliveryService.findByAddedToCartState();
//
//        GenericSpecification<SaleOrder> specification = new GenericSpecification<>();
//        specification.add(new SearchCriteria("user", requestedUser.getId(), SearchOperation.EQUAL));
//        specification.add(new SearchCriteria("delivery", delivery.getId(), SearchOperation.EQUAL));
//        SaleOrder oldSaleOrder = saleOrderService.findOne(specification);
//
//        // update order item if exists
//        if (oldSaleOrder != null) {
//            GenericSpecification<OrderItem> orderItemGenericSpecification = new GenericSpecification<>();
//            orderItemGenericSpecification.add(new SearchCriteria("saleOrder", oldSaleOrder.getId(), SearchOperation.EQUAL));
//            orderItemGenericSpecification.add(new SearchCriteria("product", product.getId(), SearchOperation.EQUAL));
//
//            OrderItem oldOrderItem = orderItemService.findOne(orderItemGenericSpecification);
//
//            OrderItem newOrderItem;
//
//            if (oldOrderItem != null) {
//                oldOrderItem.setQuantity(oldOrderItem.getQuantity() + orderItemDTO.getQuantity());
//                newOrderItem = orderItemService.createOrUpdate(oldOrderItem);
//            } else {
//                OrderItem orderItem = new OrderItem();
//                orderItem.setSaleOrder(oldSaleOrder);
//                orderItem.setProduct(product);
//                orderItem.setQuantity(orderItemDTO.getQuantity());
//                newOrderItem = orderItemService.createOrUpdate(orderItem);
//            }
//
//            return this.resSuccess(newOrderItem);
//        }
//
//        // create new sale order and order item
//        SaleOrder saleOrder = new SaleOrder();
//        saleOrder.setUser(requestedUser);
//        saleOrder.setDelivery(delivery);
//        saleOrder.setCustomerAddress(requestedUser.getAddress());
//        saleOrder.setPhone(requestedUser.getPhone());
//
//        SaleOrder newSaleOrder = saleOrderService.create(saleOrder);
//
//        OrderItem orderItem = new OrderItem();
//        orderItem.setSaleOrder(newSaleOrder);
//        orderItem.setProduct(product);
//        orderItem.setQuantity(orderItemDTO.getQuantity());
//
//        OrderItem newOrderItem = orderItemService.createOrUpdate(orderItem);
//
//        return this.resSuccess(newOrderItem);
//    }
}