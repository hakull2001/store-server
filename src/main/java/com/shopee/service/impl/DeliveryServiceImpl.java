package com.shopee.service.impl;

import com.shopee.constants.Common;
import com.shopee.entity.DeliveryEntity;
import com.shopee.repositories.DeliveryRepository;
import com.shopee.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public List<DeliveryEntity> getAll() {
        return deliveryRepository.findAll();
    }


    @Override
    public Long countAll() {
        return deliveryRepository.count();
    }

    @Override
    public DeliveryEntity findById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId).orElse(null);
    }

    @Override
    public DeliveryEntity findByIndex(String index) {
        return deliveryRepository.findByIndex(index);
    }

    @Override
    public DeliveryEntity findByAddedToCartState() {
        return this.findByIndex(Common.DELIVERY_ADDED_TO_CART_INDEX);
    }

    @Override
    public DeliveryEntity findByWaitingToConfirmState() {
        return this.findByIndex(Common.DELIVERY_WAITING_TO_CONFIRM_INDEX);
    }

    @Override
    public DeliveryEntity findByDeliveredState() {
        return this.findByIndex(Common.DELIVERY_DELIVERED_INDEX);
    }

    @Override
    public DeliveryEntity findByCancelState() {
        return this.findByIndex(Common.DELIVERY_CANCELED_INDEX);
    }


    @Override
    @Transactional
    public void seedData() {
        DeliveryEntity delivery1 = new DeliveryEntity(null, Common.DELIVERY_ADDED_TO_CART_INDEX, Common.DELIVERY_ADDED_TO_CART_VALUE, null, null, null);
        DeliveryEntity delivery2 = new DeliveryEntity(null, Common.DELIVERY_WAITING_TO_CONFIRM_INDEX, Common.DELIVERY_WAITING_TO_CONFIRM_VALUE, null, null, null);
        DeliveryEntity delivery3 = new DeliveryEntity(null, Common.DELIVERY_DELIVERING_INDEX, Common.DELIVERY_DELIVERING_VALUE, null, null, null);
        DeliveryEntity delivery4 = new DeliveryEntity(null, Common.DELIVERY_DELIVERED_INDEX, Common.DELIVERY_DELIVERED_VALUE, null, null, null);
        DeliveryEntity delivery5 = new DeliveryEntity(null, Common.DELIVERY_CANCELED_INDEX, Common.DELIVERY_CANCELED_VALUE, null, null, null);
        deliveryRepository.saveAll(Arrays.asList(delivery1, delivery2, delivery3, delivery4, delivery5));
    }
}
