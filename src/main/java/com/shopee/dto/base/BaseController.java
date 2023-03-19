package com.shopee.dto.base;

import com.shopee.dto.PaginateDTO;
import com.shopee.dto.PaginationDTO;
import com.shopee.dto.PaginationResponseDTO;
import com.shopee.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController<T> {
    public ResponseEntity<?> resSuccess(T data) {
        Map<String, T> map = new HashMap<>();
        map.put("data", data);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>(HttpStatus.OK.value(), "Success", map)
        );
    }

    public ResponseEntity<?> resListSuccess(List<?> data) {
        Map<String, List<?>> map = new HashMap<>();
        map.put("data", data);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseDTO<>(HttpStatus.OK.value(), "Success", map)
        );
    }

    public ResponseEntity<?> resPagination(PaginateDTO<?> paginateDTO) {
        PaginationDTO<List<?>> paginationDTO = new PaginationDTO<>(
                paginateDTO.getPageData().getContent(),
                paginateDTO.getPagination()
        );
        return ResponseEntity.status(HttpStatus.OK).body(
                new PaginationResponseDTO<>(HttpStatus.OK.value(), "Success", paginationDTO)
        );
    }
}