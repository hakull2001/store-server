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
    private Long budget;
    private Set<Long> userIds;
    private Integer taskProgress;
    private Long totalUserPurchase;
    private Long ordersAmount;
    private Long month;
}
