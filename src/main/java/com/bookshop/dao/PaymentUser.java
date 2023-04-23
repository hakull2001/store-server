package com.bookshop.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_users")
@Entity
public class PaymentUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vnpAmount;

    private String vnpBankCode;

    private String vnpOrderInfo;

    private String vnpPayDate;

    private String vnpTxnref;

    private String vnpResponseCode;

    private String orderId;

    private Timestamp createdAt;

    private Long paymentStatus;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
