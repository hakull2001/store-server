package com.bookshop.controllers;

import com.bookshop.base.BaseController;
import com.bookshop.dao.Delivery;
import com.bookshop.dao.User;
import com.bookshop.dto.DasboardDTO;
import com.bookshop.services.DeliveryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@SecurityRequirement(name = "Authorization")
public class DeliveryController extends BaseController<Delivery> {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping

    public ResponseEntity<?> getAllDeliveries() {
        List<Delivery> deliveries = deliveryService.findAll();
        return this.resListSuccess(deliveries);
    }
}
