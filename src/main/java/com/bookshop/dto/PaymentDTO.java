package com.bookshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String vnpAmount;

    private String vnpBankCode;

    private String vnpOrderInfo;

    private String vnpPayDate;

    private String vnpTxnref;

    private String vnpResponseCode;

    private Instant fromDate;

    private Instant toDate;

    private String orderId;

    private Long paymentStatus;

    private Long userId;
}
