package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Entity.Products;
import com.example.Du_An_TTS_Test.Repository.ProductsRepo;
import com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice.ProductsElasticsearchSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductsSevice {
    @Autowired
    private ProductsRepo productsRepo;

    @Cacheable(value = "Products", key = "#id")
    public Products findbyidProduct(Integer id) {
        Products products = productsRepo.findById(id).orElse(null);
        return products;
    }

    @Cacheable(value = "Products", key = "#id")
    public Products findbyidProducts(Integer id) {
        Products products = productsRepo.findById(id).orElse(null);
        return products;
    }

    @CachePut(value = "Products", key = "#products.id")
    public Products addProduct(Products products) {
        Products savedProduct = productsRepo.save(products);

        return savedProduct;
    }


    @CacheEvict(value = "Products", key = "#id")
    public void deleteProduct(Integer id) {
        productsRepo.deleteById(id);
    }

    //    lắng nghe kafka

    public Products UPdateview(Integer ID) {
        Products products = findbyidProduct(ID);
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
        }).orElse(null);
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
        }).orElse(null);
    }

}
