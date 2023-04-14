package com.shopee.controller;

import com.shopee.constants.Common;
import com.shopee.dto.DeliveryDTO;
import com.shopee.dto.PaginateDTO;
import com.shopee.dto.base.BaseController;
import com.shopee.entity.*;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.NotFoundException;
import com.shopee.service.DeliveryService;
import com.shopee.service.ProductService;
import com.shopee.service.SaleOrderService;
import com.shopee.service.UserService;
import com.shopee.specification.GenericSpecification;
import com.shopee.specification.SearchCriteria;
import com.shopee.specification.SearchOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/sale-orders")
@SecurityRequirement(name = "Authorization")
public class SaleOrderController extends BaseController<SaleOrderEntity> {

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    public ResponseEntity<?> getListSaleOrdersForMember(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "perPage", required = false) Integer perPage,
            @RequestParam(name = "deliveryIndex", required = false) String deliveryIndex,
            HttpServletRequest request) {
        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");

        DeliveryEntity deliveryAddedToCart = deliveryService.findByAddedToCartState();

        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<SaleOrderEntity>().getBasicQuery(request);
        specification.add(new SearchCriteria("delivery", deliveryAddedToCart.getId(), SearchOperation.NOT_EQUAL));
        specification.add(new SearchCriteria("user", requestedUser.getUserId(), SearchOperation.EQUAL));

        if (deliveryIndex != null) {
            DeliveryEntity deliverySearch = deliveryService.findByIndex(deliveryIndex);
            if (deliverySearch != null) {
                specification.add(new SearchCriteria("delivery", deliverySearch.getId(), SearchOperation.EQUAL));
            }
        }

        PaginateDTO<SaleOrderEntity> paginateSaleOrders = saleOrderService.getList(page, perPage, specification);

        return this.resPagination(paginateSaleOrders);
    }

    @GetMapping("/admin")
    @PreAuthorize("@userAuthorizer.isAdmin(authentication)")
    public ResponseEntity<?> getListSaleOrdersForAdmin(@RequestParam(name = "page", required = false) Integer page,
                                                       @RequestParam(name = "perPage", required = false) Integer perPage,
                                                       @RequestParam(name = "fetchType", required = false) Integer fetchType,
                                                       @RequestParam(name = "fromDate", required = false) String fromDate,
                                                       @RequestParam(name = "toDate", required = false) String toDate,
                                                       HttpServletRequest request) {
        DeliveryEntity deliveryAddedToCart = deliveryService.findByAddedToCartState();

        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<SaleOrderEntity>().getBasicQuery(request);
        specification.add(new SearchCriteria("delivery", deliveryAddedToCart.getId(), SearchOperation.NOT_EQUAL));

        if (fetchType != null && fetchType.equals(Common.FETCH_TYPE_ADMIN)) {
            DeliveryEntity deliveryDelivered = deliveryService.findByDeliveredState();
            specification.add(new SearchCriteria("delivery", deliveryDelivered.getId(), SearchOperation.EQUAL));
        }

        if (fromDate != null && toDate == null) {
            specification.add(new SearchCriteria("orderedAt", fromDate, SearchOperation.GREATER_THAN_EQUAL));
        } else if (fromDate == null && toDate != null) {
            specification.add(new SearchCriteria("orderedAt", toDate, SearchOperation.LESS_THAN_EQUAL));
        } else if (fromDate != null) {
            specification.add(new SearchCriteria("orderedAt", fromDate, SearchOperation.GREATER_THAN_EQUAL));
            specification.add(new SearchCriteria("orderedAt", toDate, SearchOperation.LESS_THAN_EQUAL));
        }

        PaginateDTO<SaleOrderEntity> paginateSaleOrders = saleOrderService.getList(page, perPage, specification);

        return this.resPagination(paginateSaleOrders);
    }

    @GetMapping("/{saleOrderId}")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    public ResponseEntity<?> getSaleOrderByIdForMember(@PathVariable("saleOrderId") Long saleOrderId, HttpServletRequest request) {
        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");

        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<SaleOrderEntity>().getBasicQuery(request);
        specification.add(new SearchCriteria("user", requestedUser.getUserId(), SearchOperation.EQUAL));
        specification.add(new SearchCriteria("id", saleOrderId, SearchOperation.EQUAL));

        SaleOrderEntity saleOrder = saleOrderService.findOne(specification);

        return this.resSuccess(saleOrder);
    }

    @GetMapping("/admin/{saleOrderId}")
    @PreAuthorize("@userAuthorizer.isAdmin(authentication)")
    public ResponseEntity<?> getSaleOrderByIdForAdmin(@PathVariable("saleOrderId") Long saleOrderId) {
        SaleOrderEntity saleOrder = saleOrderService.findById(saleOrderId);

        return this.resSuccess(saleOrder);
    }

    @PatchMapping("/{saleOrderId}")
    @PreAuthorize("@userAuthorizer.isAdmin(authentication)")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateSaleOrderDelivery(@PathVariable("saleOrderId") Long saleOrderId,
                                                     @RequestBody @Valid DeliveryDTO deliveryDTO) {
        DeliveryEntity deliveryAddedToCart = deliveryService.findByAddedToCartState();
        DeliveryEntity deliveryCancel = deliveryService.findByCancelState();

        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<>();
        specification.add(new SearchCriteria("delivery", deliveryAddedToCart.getId(), SearchOperation.NOT_EQUAL));
        specification.add(new SearchCriteria("delivery", deliveryCancel.getId(), SearchOperation.NOT_EQUAL));
        specification.add(new SearchCriteria("id", saleOrderId, SearchOperation.EQUAL));

        SaleOrderEntity saleOrder = saleOrderService.findOne(specification);

        if (saleOrder == null) {
            throw new NotFoundException("Not found sale order");
        }

        DeliveryEntity delivery = deliveryService.findById(deliveryDTO.getDeliveryId());

        if (delivery == null) {
            throw new NotFoundException("Not found delivery");
        }

        saleOrder.setDelivery(delivery);
        SaleOrderEntity savedSaleOrder = saleOrderService.update(saleOrder);

        if (delivery.getIndex().equals(Common.DELIVERY_CANCELED_INDEX)) {
            List<OrderItemEntity> orderItems = saleOrder.getOrderItems();

            for (OrderItemEntity orderItem : orderItems) {
                ProductEntity product = orderItem.getProduct();
                product.setSold(product.getSold() + orderItem.getQuantity());
                productService.update(product);
            }
        }

        return this.resSuccess(savedSaleOrder);
    }

    @PatchMapping("/{saleOrderId}/payment")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> handlePaymentSaleOrder(@PathVariable("saleOrderId") Long saleOrderId,
                                                    HttpServletRequest request) {
        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");

        DeliveryEntity delivery = deliveryService.findByAddedToCartState();

        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<>();
        specification.add(new SearchCriteria("user", requestedUser.getUserId(), SearchOperation.EQUAL));
        specification.add(new SearchCriteria("delivery", delivery.getId(), SearchOperation.EQUAL));
        specification.add(new SearchCriteria("id", saleOrderId, SearchOperation.EQUAL));

        SaleOrderEntity saleOrder = saleOrderService.findOne(specification);

        if (saleOrder == null) {
            throw new NotFoundException("Not found sale order");
        }

        List<OrderItemEntity> orderItems = saleOrder.getOrderItems();

        for (int i = 0; i < orderItems.size(); i++) {
            ProductEntity product = orderItems.get(i).getProduct();
            if (product.getSold() < orderItems.get(i).getQuantity()) {
                throw new AppException("Not enough quantity");
            }
        }

        for (int i = 0; i < orderItems.size(); i++) {
            ProductEntity product = orderItems.get(i).getProduct();
            product.setSold(product.getSold() - orderItems.get(i).getQuantity());
            productService.update(product);
        }


        DeliveryEntity deliveryWaitingToConfirm = deliveryService.findByWaitingToConfirmState();

        saleOrder.setDelivery(deliveryWaitingToConfirm);
        saleOrder.setOrderedAt(new Timestamp(new Date().getTime()));
        SaleOrderEntity newSaleOrder = saleOrderService.update(saleOrder);

        return this.resSuccess(newSaleOrder);
    }

    @DeleteMapping("/{saleOrderId}")
    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> cancelSaleOrder(@PathVariable("saleOrderId") Long saleOrderId,
                                             HttpServletRequest request) {
        UserShopEntity requestedUser = (UserShopEntity) request.getAttribute("user");

        DeliveryEntity deliveryWaitingToConfirm = deliveryService.findByWaitingToConfirmState();

        GenericSpecification<SaleOrderEntity> specification = new GenericSpecification<>();
        specification.add(new SearchCriteria("user", requestedUser.getUserId(), SearchOperation.EQUAL));
        specification.add(new SearchCriteria("delivery", deliveryWaitingToConfirm.getId(), SearchOperation.EQUAL));
        specification.add(new SearchCriteria("id", saleOrderId, SearchOperation.EQUAL));

        SaleOrderEntity saleOrder = saleOrderService.findOne(specification);

        if (saleOrder == null) {
            throw new NotFoundException("Not found sale order");
        }

        DeliveryEntity deliveryCancel = deliveryService.findByCancelState();

        saleOrder.setDelivery(deliveryCancel);

        List<OrderItemEntity> orderItems = saleOrder.getOrderItems();

        for (OrderItemEntity orderItem : orderItems) {
            ProductEntity product = orderItem.getProduct();
            product.setSold(product.getSold() + orderItem.getQuantity());
            productService.update(product);
        }

        SaleOrderEntity newSaleOrder = saleOrderService.update(saleOrder);

        return this.resSuccess(newSaleOrder);
    }
}