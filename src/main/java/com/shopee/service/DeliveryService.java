package com.shopee.service;

import com.shopee.entity.DeliveryEntity;

import java.util.List;

public interface DeliveryService {
    List<DeliveryEntity> getAll();

    Long countAll();

    DeliveryEntity findById(Long deliveryId);

    DeliveryEntity findByIndex(String index);

    DeliveryEntity findByAddedToCartState();

    DeliveryEntity findByWaitingToConfirmState();

    DeliveryEntity findByDeliveredState();

    DeliveryEntity findByCancelState();

    void seedData();

}
