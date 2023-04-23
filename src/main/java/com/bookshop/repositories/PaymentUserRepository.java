package com.bookshop.repositories;

import com.bookshop.dao.PaymentUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentUserRepository extends JpaRepository<PaymentUser, Long>, JpaSpecificationExecutor<PaymentUser> {
    PaymentUser findByVnpTxnref(String vnpayTxnref);

    List<PaymentUser> findByUserId(Long userId);

    List<PaymentUser> findByUserIdAndPaymentStatus(Long userId, Long paymentStatus);
}
