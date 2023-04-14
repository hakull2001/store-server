package com.shopee.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class VnPayRequest {
    private String vnpVersion;
    private String vnpCommand;
    private String vnpTmnCode;
    private Long vnpAmount;
    private  String vnpBankCode;
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
