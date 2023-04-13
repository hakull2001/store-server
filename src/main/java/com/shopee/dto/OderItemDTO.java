package com.shopee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OderItemDTO {
    @NotNull
    @Min(1)
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private String color;
}
