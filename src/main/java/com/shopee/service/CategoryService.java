package com.shopee.service;

import com.shopee.entity.CategoryEntity;
import com.shopee.request.clothes.SearchClothesRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryEntity> getAllCategories();

    CategoryEntity findById(Long categoryId, HttpServletRequest servletRequest, SearchClothesRequest request);
}
