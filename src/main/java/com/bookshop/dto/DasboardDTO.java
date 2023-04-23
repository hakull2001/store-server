package com.bookshop.dto;

import com.bookshop.dao.Delivery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DasboardDTO {
    private Long budget;
    private Long totalCustom;
    private Integer taskProgress;
    List<Delivery> deliveries;
}
