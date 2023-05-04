package com.bookshop.dto;

import com.bookshop.dao.Delivery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DasboardDTO {
    private Long saleOrders;
    private Long budget;
    private Long products;
    private Long month;
    private Long customers;
}
