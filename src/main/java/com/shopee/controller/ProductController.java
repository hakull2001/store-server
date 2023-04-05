package com.shopee.controller;

import com.shopee.dto.PaginateDTO;
import com.shopee.dto.base.BaseController;
import com.shopee.entity.ProductEntity;
import com.shopee.request.clothes.SearchClothesRequest;
import com.shopee.response.ResultResponse;
import com.shopee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController<ProductEntity> {

    @Autowired
    private ProductService clothesService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAllProductByCategory(@PathVariable("id") Long categoryId, SearchClothesRequest searchRequest, HttpServletRequest request) {
        PaginateDTO<ProductEntity> paginateProducts = clothesService.findAllByCategoryId(searchRequest.getPage(), searchRequest.getPerPage(), request, searchRequest, categoryId);

        return this.resPagination(paginateProducts);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailProduct(@PathVariable("id") Long productId) {
        return this.resSuccess(clothesService.getDetailProduct(productId));
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(SearchClothesRequest searchClothesRequest, HttpServletRequest request){
        return this.resPagination(clothesService.getAll(searchClothesRequest.getPage(), searchClothesRequest.getPerPage(), request, searchClothesRequest));
    }

}
