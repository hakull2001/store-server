package com.bookshop.repositories;

import com.bookshop.dao.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findByIndex(String index);

    List<Delivery> findByIndexIs(String index);
}
