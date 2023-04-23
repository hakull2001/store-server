package com.bookshop.services;

import com.bookshop.dao.PaymentUser;
import com.bookshop.dto.pagination.PaginateDTO;
import com.bookshop.specifications.GenericSpecification;

import java.util.List;

public interface PaymentUserService {
    void create(PaymentUser paymentUser);

    PaymentUser findByVnpTxnRef(String vnpTxnRef);

    void update(PaymentUser paymentUser);

    PaginateDTO<PaymentUser> getListPayments(Integer page, Integer perPage, GenericSpecification<PaymentUser> specification);

    PaginateDTO<PaymentUser> getList(Integer page, Integer perPage, GenericSpecification<PaymentUser> specification);

    Long totalPayment(Long userId);
}
