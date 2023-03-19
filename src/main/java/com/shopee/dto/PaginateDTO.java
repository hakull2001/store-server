package com.shopee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginateDTO<T> {
    private Page<T> pageData;
    private PaginationDTO.Pagination pagination;
}
