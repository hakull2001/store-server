package com.shopee.service.impl;

import com.shopee.dto.PaginateDTO;
import com.shopee.dto.PaginationDTO;
import com.shopee.dto.base.BasePagination;
import com.shopee.entity.CategoryEntity;
import com.shopee.entity.ProductEntity;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.NotFoundException;
import com.shopee.repositories.CategoryRepository;
import com.shopee.repositories.ProductRepository;
import com.shopee.request.clothes.SearchClothesRequest;
import com.shopee.service.ProductService;
import com.shopee.specification.GenericSpecification;
import com.shopee.specification.JoinCriteria;
import com.shopee.specification.SearchCriteria;
import com.shopee.specification.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class ProductServiceImpl extends BasePagination<ProductEntity, ProductRepository> implements ProductService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository clothesRepository;

    @Autowired
    private ProductServiceImpl(ProductRepository clothesRepository) {
        super(clothesRepository);
    }

    @Override
    public PaginateDTO<ProductEntity> findAllByCategoryId(Integer page, Integer perPage, HttpServletRequest servletRequest, SearchClothesRequest searchClothesRequest, Long categoryId) {
        GenericSpecification<ProductEntity> specification = new GenericSpecification<ProductEntity>().getBasicQuery(servletRequest);
        Optional<CategoryEntity> categoryEntityOpt = categoryRepository.findById(categoryId);

        categoryEntityOpt.ifPresentOrElse(category -> {
            specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "category", "id",
                    categoryId, JoinType.INNER));
        }, () -> {
            throw new AppException("Not found this category ");
        });

        if (searchClothesRequest.getFromPrice() != null) {
            specification.add(new SearchCriteria("price", searchClothesRequest.getFromPrice(), SearchOperation.GREATER_THAN_EQUAL));
        }
        if (searchClothesRequest.getToPrice() != null) {
            specification.add(new SearchCriteria("price", searchClothesRequest.getToPrice(), SearchOperation.LESS_THAN_EQUAL));
        }
        PaginateDTO<ProductEntity> paginate = this.paginate(page, perPage, specification);
        if (paginate.getPageData().getTotalElements() == 0)
            throw new NotFoundException("Not found clothes by this ");
        return paginate;
    }

    @Override
    public Optional<ProductEntity> getDetailProduct(Long productId) {
        Optional<ProductEntity> clothesEntityOpt = clothesRepository.findById(productId);
        if(clothesEntityOpt.isPresent())
            return clothesEntityOpt;
        throw new AppException("Not found this clothes");
    }

    @Override
    public PaginationDTO<ProductEntity> getAll(Integer page, Integer perPage, HttpServletRequest servletRequest, SearchClothesRequest searchClothesRequest) {
        return null;
    }
}
