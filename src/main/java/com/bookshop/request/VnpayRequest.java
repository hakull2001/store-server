package com.bookshop.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnpayRequest {
    private String vnpVersion;
    private String vnpCommand;
    private String vnpTmnCode;
    private String vnpAmount;
    private String vnpBankCode;
    private Instant vnpCreateDate;
    private String vnpCurrCode;
    private String vnpIpAddr;
    private String vnpLocale;
    private String vnpOrderInfo;
    private String vnpOrderType;
    private String vnpReturnUrl;
    private String vnpTxnRef;
    private String vnpSecureHash;
}
