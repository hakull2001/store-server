package com.shopee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "clothes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Long amount;

    private String color;

    private Long price;

    private Long sold;

    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    private CategoryEntity category;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductRateEntity> productRates;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImageEntity> productImages;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItemEntity> orderItemEntities;
}
