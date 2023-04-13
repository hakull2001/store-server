package com.shopee.request.clothes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchClothesRequest {
    private Long fromPrice;
    private Long toPrice;
    private String name;
    private String size;
    private String color;
    private Long rating;
    private Integer page;
    private Integer perPage;
}
