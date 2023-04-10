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

    @GetMapping(value = "/{name}")
    public ResponseEntity<?> getAllProductByCategory(@PathVariable("name") String name, SearchClothesRequest searchRequest, HttpServletRequest request) {
        PaginateDTO<ProductEntity> paginateProducts = clothesService.findAllByCategoryId(searchRequest.getPage(), searchRequest.getPerPage(), request, searchRequest, name);

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
