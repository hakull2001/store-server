package com.shopee.controller;

import com.shopee.response.ResultResponse;
import com.shopee.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResultResponse getAllCategories(){
        return ResultResponse.SUCCESS.withResult(categoryService.getAllCategories());
    }
}
