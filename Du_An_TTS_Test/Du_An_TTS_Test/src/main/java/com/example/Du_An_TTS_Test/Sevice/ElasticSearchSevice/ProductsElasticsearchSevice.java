package com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice;

import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Entity.Products;
import com.example.Du_An_TTS_Test.Map.ProductsMapper;
import com.example.Du_An_TTS_Test.Repository.ProductElasticsearchRepository;
import com.example.Du_An_TTS_Test.Repository.ProductsRepo;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Autowired
    private ProductsRepo productsRepo;

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
            ProductsElasticsearch product = objectMapper.readValue(logMessage, ProductsElasticsearch.class);


            elasticsearchRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @KafkaListener(topics = "updateViewID")
    public void listen(String messange) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Products product = objectMapper.readValue(messange, Products.class);

        ProductsElasticsearch existingProductOpt = elasticsearchRepository.findById(product.getId()).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));

        try {
            ProductsElasticsearch existingProduct = existingProductOpt;
            existingProduct.setView(product.getView().intValue() + 1);
            elasticsearchRepository.save(existingProduct);
        } catch (NumberFormatException e) {
            System.err.println("Invalid view number: ");
        }


    }

    @KafkaListener(topics = "updateProduct", groupId = "products-group")
    public void updateProduct(String logMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Products product = objectMapper.readValue(logMessage, Products.class);

            ProductsElasticsearch existingProductOpt = elasticsearchRepository.findById(product.getId()).orElseThrow(()
                    -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));

            ProductsElasticsearch existingProduct = existingProductOpt;

            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setStock_quantity(product.getStock_quantity());
            existingProduct.setCreated_at(product.getCreated_at());
            existingProduct.setCreated_by(product.getCreated_by());
            existingProduct.setUpdated_at(product.getUpdated_at());
            existingProduct.setView(product.getView());

            elasticsearchRepository.save(existingProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
