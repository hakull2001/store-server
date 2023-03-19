package com.shopee.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterSearch {

    private Integer page;

    private Integer size;

    private Integer fromId;

    private Integer toId;
}
