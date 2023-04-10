package com.shopee.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ProductDTO {
    private String name;

    private String description;

    private Long amount;

    private String color;

    private Long price;

    private Long sold;

    private Instant createdDate;

    private String category;
}
