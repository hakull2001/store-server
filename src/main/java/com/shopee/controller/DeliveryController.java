package com.shopee.controller;

import com.shopee.dto.base.BaseController;
import com.shopee.entity.DeliveryEntity;
import com.shopee.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController extends BaseController<DeliveryEntity> {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<?> getAllDeliveries() {
        List<DeliveryEntity> deliveries = deliveryService.getAll();
        return this.resListSuccess(deliveries);
    }
}
