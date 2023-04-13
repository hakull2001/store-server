package com.shopee.request;

import lombok.Data;
import lombok.NonNull;

@Data
public class DeleteItemCartRequest {
    @NonNull
    private Long id;
}
