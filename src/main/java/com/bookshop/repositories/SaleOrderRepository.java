package com.bookshop.repositories;

import com.bookshop.dao.Delivery;
import com.bookshop.dao.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long>, JpaSpecificationExecutor<SaleOrder> {
    List<SaleOrder> findByDeliveryIn(List<Delivery> deliveries);
}
