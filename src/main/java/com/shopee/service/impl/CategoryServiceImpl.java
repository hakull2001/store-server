package com.shopee.service.impl;

import com.shopee.dto.base.BasePagination;
import com.shopee.entity.CategoryEntity;
import com.shopee.entity.ProductEntity;
import com.shopee.repositories.CategoryRepository;
import com.shopee.repositories.ProductRepository;
import com.shopee.request.clothes.SearchClothesRequest;
import com.shopee.service.CategoryService;
import com.shopee.specification.GenericSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl extends BasePagination<CategoryEntity, CategoryRepository> implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository clothesRepository;

    @Autowired
    private CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
    }
    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity findById(Long categoryId, HttpServletRequest servletRequest, SearchClothesRequest request) {
        GenericSpecification<ProductEntity> specification = new GenericSpecification<ProductEntity>().getBasicQuery(servletRequest);
        Optional<CategoryEntity> categoryOpt = categoryRepository.findById(categoryId);
        categoryOpt.ifPresentOrElse(category -> {

        }, () -> {});
        return null;
    }
}
