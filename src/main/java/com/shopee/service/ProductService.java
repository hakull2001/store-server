package com.shopee.service;

import com.shopee.dto.PaginateDTO;
import com.shopee.dto.PaginationDTO;
import com.shopee.entity.ProductEntity;
import com.shopee.request.clothes.SearchClothesRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface ProductService {
    PaginateDTO<ProductEntity> findAllByCategoryId(Integer page, Integer perPage, HttpServletRequest servletRequest, SearchClothesRequest searchClothesRequest, Long categoryId);

    Optional<ProductEntity> getDetailProduct(Long productId);

    PaginationDTO<ProductEntity> getAll(Integer page, Integer perPage, HttpServletRequest servletRequest, SearchClothesRequest searchClothesRequest);
}
