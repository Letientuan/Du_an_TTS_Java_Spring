package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Entity.Products;
import com.example.Du_An_TTS_Test.Map.ProductsMapper;
import com.example.Du_An_TTS_Test.Repository.ProductsRepo;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductsSevice {

    @Autowired
    private ProductsRepo productsRepo;

    @Cacheable(value = "Products", key = "#id")
    public Products findbyidProduct(Integer id) {
        Products products = productsRepo.findById(id).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
        return products;
    }

    @Cacheable(value = "Products", key = "#id", unless = "#result == null")
    public Products findbyidProducts(Integer id) {
        Optional<Products> productOptional = productsRepo.findById(id);

        return productOptional.orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
    }

    @CachePut(value = "Products", key = "#products.id")
    public Products addProduct(Products products) {
        Products savedProduct = productsRepo.save(products);
        return savedProduct;
    }


    @CacheEvict(value = "Products", key = "#id")
    public void deleteProduct(Integer id) {
        Products products = findbyidProducts(id);
        productsRepo.deleteById(products.getId());
    }


    public Products updateView(Integer ID) {
        Products products = findbyidProduct(ID);
        return upDate(ID, products.getView().intValue());
    }

    @CacheEvict(value = "Products", key = "#id")
    public Products upDate(Integer id, Number view) {
        Optional<Products> optional = productsRepo.findById(id);
        return optional.map(o -> {
            o.setView(view.intValue() + 1);
            Products products = productsRepo.save(o);
            return products;
        }).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
    }

    @CacheEvict(value = "Products", key = "#id")
    public Products updateProduct(Integer id, ProductsElasticsearch products) {
        Products optional = productsRepo.findById(id).get();
        ProductsMapper.PRODUCTS_MAPPER.updateEntityFromDto(products, optional);
        return productsRepo.save(optional);
    }

}
