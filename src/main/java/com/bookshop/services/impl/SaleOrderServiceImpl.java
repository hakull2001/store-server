package com.bookshop.services.impl;

import com.bookshop.base.BasePagination;
import com.bookshop.constants.Common;
import com.bookshop.dao.Delivery;
import com.bookshop.dao.OrderItem;
import com.bookshop.dao.SaleOrder;
import com.bookshop.dto.DasboardDTO;
import com.bookshop.dto.pagination.PaginateDTO;
import com.bookshop.repositories.DeliveryRepository;
import com.bookshop.repositories.SaleOrderRepository;
import com.bookshop.services.SaleOrderService;
import com.bookshop.specifications.GenericSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleOrderServiceImpl extends BasePagination<SaleOrder, SaleOrderRepository> implements SaleOrderService {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    public SaleOrderServiceImpl(SaleOrderRepository saleOrderRepository) {
        super(saleOrderRepository);
    }

    @Override
    public SaleOrder findOne(GenericSpecification<SaleOrder> specification) {
        return saleOrderRepository.findOne(specification).orElse(null);
    }

    @Override
    public SaleOrder findById(Long saleOrderId) {
        return saleOrderRepository.findById(saleOrderId).orElse(null);
    }

    @Override
    public SaleOrder create(SaleOrder saleOrder) {
        return saleOrderRepository.save(saleOrder);
    }

    @Override
    public SaleOrder update(SaleOrder saleOrder) {
        return saleOrderRepository.save(saleOrder);
    }

    @Override
    public void deleteById(Long saleOrderId) {
        saleOrderRepository.deleteById(saleOrderId);
    }

    @Override
    public Long calculateTotalAmount(List<OrderItem> orderItems) {
        long totalAmount = 0L;
        for (OrderItem orderItem : orderItems) {
            totalAmount += orderItem.getProduct().getPrice() * orderItem.getQuantity();
        }
        return totalAmount;
    }

    @Override
    public List<SaleOrder> getALl() {
        List<Delivery> deliveries = deliveryRepository.findByIndexNotIn(List.of(Common.DELIVERY_CANCELED_INDEX, Common.DELIVERY_ADDED_TO_CART_INDEX));
        LocalDate today = LocalDate.now();
        Timestamp startOfDay = Timestamp.valueOf(today.atStartOfDay());
        List<SaleOrder> saleOrders = saleOrderRepository.findByDeliveryInAndOrderedAtBetween(deliveries, startOfDay, Timestamp.valueOf(LocalDateTime.now()));
        return saleOrders;
    }

    @Override
    public List<DasboardDTO> getAllMonths() {
        int year = LocalDateTime.now().getYear();
        List<LocalDateTime> times = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            times.add(startOfMonth);
        }
        List<DasboardDTO> dasboardDTOS = new ArrayList<>();
        List<Delivery> deliveries = deliveryRepository.findByIndexNotIn(List.of(Common.DELIVERY_CANCELED_INDEX, Common.DELIVERY_ADDED_TO_CART_INDEX));

        times.forEach(time -> {
            List<SaleOrder> saleOrders = new ArrayList<>();
            Long budget = 0L;
            DasboardDTO dasboardDTO = new DasboardDTO();
                saleOrders = saleOrderRepository.findByDeliveryInAndOrderedAtBetween(deliveries, Timestamp.valueOf(time), Timestamp.valueOf(time.plus(1, ChronoUnit.MONTHS)));
                if(!saleOrders.isEmpty())
                for (SaleOrder saleOrder : saleOrders) {
                    for (OrderItem orderItem : saleOrder.getOrderItems()) {
                        budget += (orderItem.getQuantity() * orderItem.getProduct().getPrice());
                    }
                }
            dasboardDTO.setBudget(budget);
                dasboardDTO.setMonth((long) time.getMonthValue());
            dasboardDTOS.add(dasboardDTO);
        });
        return dasboardDTOS;
    }


    @Override
    public PaginateDTO<SaleOrder> getList(Integer page, Integer perPage, GenericSpecification<SaleOrder> specification) {
        return this.paginate(page, perPage, specification);
    }

    @Override
    public List<SaleOrder> getList() {
        List<Delivery> deliveries = deliveryRepository.findByIndexNotIn(List.of(Common.DELIVERY_ADDED_TO_CART_INDEX));
        List<SaleOrder> saleOrders = saleOrderRepository.findByDeliveryIn(deliveries);
        return saleOrders;
    }
}
