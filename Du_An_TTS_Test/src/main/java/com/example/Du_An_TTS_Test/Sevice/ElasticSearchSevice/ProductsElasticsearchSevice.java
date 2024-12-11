package com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice;

import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Entity.Products;
import com.example.Du_An_TTS_Test.Repository.ProductElasticsearchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductsElasticsearchSevice {

    @Autowired
    private ProductElasticsearchRepository elasticsearchRepository;

    public Iterable<ProductsElasticsearch> getProducts() {
        return elasticsearchRepository.findAll();
    }

    @KafkaListener(topics = "deleteProduct")
    public void deleteProduct(String id) {
        elasticsearchRepository.deleteById(Integer.valueOf(id));
    }

    @KafkaListener(topics = "addproduct", groupId = "products-group")
    public void addProduct(String logMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Products product = objectMapper.readValue(logMessage, Products.class);


            ProductsElasticsearch productElasticsearch = new ProductsElasticsearch();
            productElasticsearch.setId(product.getId());
            productElasticsearch.setName(product.getName());
            productElasticsearch.setPrice(product.getPrice());
            productElasticsearch.setStock_quantity(product.getStock_quantity());
            productElasticsearch.setCreated_at(product.getCreated_at());
            productElasticsearch.setCreated_by(product.getCreated_by());
            productElasticsearch.setUpdated_at(product.getUpdated_at());
            productElasticsearch.setView(product.getView());

            elasticsearchRepository.save(productElasticsearch);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @KafkaListener(topics = "updateProduct", groupId = "products-group")
    public void updateProduct(String logMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Products product = objectMapper.readValue(logMessage, Products.class);

            Optional<ProductsElasticsearch> existingProductOpt = elasticsearchRepository.findById(product.getId());
            if (existingProductOpt.isPresent()) {
                ProductsElasticsearch existingProduct = existingProductOpt.get();

                existingProduct.setName(product.getName());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setStock_quantity(product.getStock_quantity());
                existingProduct.setCreated_at(product.getCreated_at());
                existingProduct.setCreated_by(product.getCreated_by());
                existingProduct.setUpdated_at(product.getUpdated_at());
                existingProduct.setView(product.getView());

                elasticsearchRepository.save(existingProduct);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
