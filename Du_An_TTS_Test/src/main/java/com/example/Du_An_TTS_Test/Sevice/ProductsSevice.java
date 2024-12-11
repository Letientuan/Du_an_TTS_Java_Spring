package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Entity.Products;
import com.example.Du_An_TTS_Test.Repository.ProductsRepo;
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

    @KafkaListener(topics = "ID")
    public void listen(String ID) {
        Integer idProduct = Integer.valueOf(ID);
        if (ID != null) {
            try {
                Products products = findbyidProduct(idProduct);
                update(idProduct, products.getView() + 1);
            } catch (NumberFormatException e) {
                System.err.println("Invalid view number: " + ID);
            }

        } else {
            System.out.println("idProduct is null, skipping update.");
        }
    }

    @CacheEvict(value = "Products", key = "#id")
    public Products update(Integer id, Integer view) {
        Optional<Products> optional = productsRepo.findById(id);
        return optional.map(o -> {
            o.setView(view);
            Products updatedProduct = productsRepo.save(o);
            return updatedProduct;
        }).orElse(null);
    }

    @CacheEvict(value = "Products", key = "#id")
    public Products updateProduct(Integer id, Products products) {
        Optional<Products> optional = productsRepo.findById(id);
        return optional.map(o -> {
            o.setView(products.getCreated_by());
            o.setPrice(products.getPrice());
            o.setCreated_at(products.getCreated_at());
            o.setName(products.getName());
            o.setUpdated_at(products.getUpdated_at());
            o.setStock_quantity(products.getStock_quantity());
            o.setCreated_by(products.getCreated_by());
            Products updatedProduct = productsRepo.save(o);
            return updatedProduct;
        }).orElse(null);
    }

}
