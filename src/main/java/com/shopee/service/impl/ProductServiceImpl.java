package com.shopee.service.impl;

import com.shopee.dto.PaginateDTO;
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
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl(ProductRepository clothesRepository) {
        super(clothesRepository);
    }

    @Override
    public PaginateDTO<ProductEntity> findAllByCategoryId(Integer page, Integer perPage, HttpServletRequest servletRequest, SearchClothesRequest searchClothesRequest, String name) {
        GenericSpecification<ProductEntity> specification = new GenericSpecification<ProductEntity>().getBasicQuery(servletRequest);
        Optional<CategoryEntity> categoryEntityOpt = categoryRepository.findByName(name);

        categoryEntityOpt.ifPresentOrElse(category -> {
            specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "category", "name",
                    name, JoinType.INNER));
        }, () -> {
//            throw new AppException("Not found this category ");
        });

        if (searchClothesRequest.getFromPrice() != null) {
            specification.add(new SearchCriteria("price", searchClothesRequest.getFromPrice(), SearchOperation.GREATER_THAN_EQUAL));
        }
        if (searchClothesRequest.getToPrice() != null) {
            specification.add(new SearchCriteria("price", searchClothesRequest.getToPrice(), SearchOperation.LESS_THAN_EQUAL));
        }
        if(searchClothesRequest.getName() != null)
            specification.add(new SearchCriteria("name", searchClothesRequest.getName(), SearchOperation.LIKE));

        PaginateDTO<ProductEntity> paginate = this.paginate(page, perPage, specification);

        return paginate;
    }

    @Override
    public ProductEntity getDetailProduct(Long productId) {
        Optional<ProductEntity> productEntityOpt = productRepository.findById(productId);
        if(productEntityOpt.isPresent())
            return productEntityOpt.get();
        throw new AppException("Not found this clothes");
    }
    @Override
    public void update(ProductEntity product) {
        productRepository.save(product);
    }
    @Override
    public PaginateDTO<ProductEntity> getAll(Integer page, Integer perPage, HttpServletRequest servletRequest, SearchClothesRequest searchClothesRequest) {
        GenericSpecification<ProductEntity> specification = new GenericSpecification<ProductEntity>().getBasicQuery(servletRequest);
        if (searchClothesRequest.getFromPrice() != null) {
            specification.add(new SearchCriteria("price", searchClothesRequest.getFromPrice(), SearchOperation.GREATER_THAN_EQUAL));
        }
        if (searchClothesRequest.getToPrice() != null) {
            specification.add(new SearchCriteria("price", searchClothesRequest.getToPrice(), SearchOperation.LESS_THAN_EQUAL));
        }
        if(searchClothesRequest.getName() != null){
            specification.add(new SearchCriteria("name", searchClothesRequest.getName(), SearchOperation.LIKE));
        }
        PaginateDTO<ProductEntity> paginate = this.paginate(page, perPage, specification);
//        if (paginate.getPageData().getTotalElements() == 0)
//            throw new NotFoundException("Not found clothes by this ");
        return paginate;
    }
}
