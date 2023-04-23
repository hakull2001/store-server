package com.bookshop.services.impl;

import com.bookshop.base.BasePagination;
import com.bookshop.dao.PaymentUser;
import com.bookshop.dto.pagination.PaginateDTO;
import com.bookshop.repositories.PaymentUserRepository;
import com.bookshop.services.PaymentUserService;
import com.bookshop.specifications.GenericSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PaymentUserServiceImpl extends BasePagination<PaymentUser, PaymentUserRepository> implements PaymentUserService {
    @Autowired
    private PaymentUserRepository paymentUserRepository;

    @Autowired
    public PaymentUserServiceImpl(PaymentUserRepository paymentUserRepository) {
        super(paymentUserRepository);
    }

    @Override
    public void create(PaymentUser paymentUser) {
        paymentUserRepository.save(paymentUser);
    }

    @Override
    public PaymentUser findByVnpTxnRef(String vnpTxnRef) {
        return paymentUserRepository.findByVnpTxnref(vnpTxnRef);
    }

    @Override
    public void update(PaymentUser paymentUser) {
        paymentUserRepository.save(paymentUser);
    }

    @Override
    public PaginateDTO<PaymentUser> getListPayments(Integer page, Integer perPage, GenericSpecification<PaymentUser> specification) {
        return this.paginate(page, perPage, specification);
    }


    @Override
    public PaginateDTO<PaymentUser> getList(Integer page, Integer perPage, GenericSpecification<PaymentUser> specification) {
        return this.paginate(page, perPage, specification);
    }

    @Override
    public Long totalPayment(Long userId) {
        AtomicReference<Long> amount = new AtomicReference<>(0L);
        List<PaymentUser> paymentUsers = paymentUserRepository.findByUserIdAndPaymentStatus(userId, 1L);
        paymentUsers.forEach(e -> {
            amount.updateAndGet(v -> v + Long.valueOf(e.getVnpAmount()));
        });
        return (amount.get());
    }


}
