package com.shopee.service;

import com.shopee.dto.PaginateDTO;
import com.shopee.entity.ProductEntity;
import com.shopee.request.clothes.SearchClothesRequest;

import javax.servlet.http.HttpServletRequest;

public interface ProductService {
    PaginateDTO<ProductEntity> findAllByCategoryId(Integer page, Integer perPage, HttpServletRequest servletRequest, SearchClothesRequest searchClothesRequest, Long categoryId);

    ProductEntity getDetailProduct(Long productId);

    void update(ProductEntity product);
    PaginateDTO<ProductEntity> getAll(Integer page, Integer perPage, HttpServletRequest servletRequest, SearchClothesRequest searchClothesRequest);
}
