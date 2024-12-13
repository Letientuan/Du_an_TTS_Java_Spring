package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Entity.Products;
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

    //    lắng nghe kafka

    public Products Updateview(Integer ID) {
        Products products = findbyidProduct(ID) ;
        if (ID != null) {
            try {

                update(ID, products.getView().intValue());
            } catch (NumberFormatException e) {
                System.err.println("Invalid view number: " + ID);
            }

        } else {
            System.out.println("idProduct is null, skipping update.");
        }
        return products;
    }

    @CacheEvict(value = "Products", key = "#id")
    public Products update(Integer id, Number view) {
        Optional<Products> optional = productsRepo.findById(id);
        return optional.map(o -> {
            o.setView(view.intValue() + 1);
            Products products = productsRepo.save(o);
            return products;
        }).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
    }

    @CacheEvict(value = "Products", key = "#id")
    public Products updateProduct(Integer id, Products products) {
        Optional<Products> optional = productsRepo.findById(id);
        return optional.map(o -> {
            o.setView(products.getView().intValue() + 1);
            o.setPrice(products.getPrice());
            o.setCreated_at(products.getCreated_at());
            o.setName(products.getName());
            o.setUpdated_at(products.getUpdated_at());
            o.setStock_quantity(products.getStock_quantity());
            o.setCreated_by(products.getCreated_by());
            Products products1 = productsRepo.save(o);
            return products1;
        }).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
    }

}
