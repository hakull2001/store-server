package com.shopee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clothes_rate")
@Entity
public class ProductRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rating;

    private String comment;

    private Instant createdDate;

    private Instant updatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserShopEntity user;

    @ManyToOne
    @JoinColumn(name = "clothes_id")
    @JsonIgnore
    private ProductEntity product;
}
