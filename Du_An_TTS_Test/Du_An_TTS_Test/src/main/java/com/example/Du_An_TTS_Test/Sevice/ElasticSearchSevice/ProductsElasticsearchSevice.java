package com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice;

import com.example.Du_An_TTS_Test.Dto.CommentDto;
import com.example.Du_An_TTS_Test.Dto.ProductDto;
import com.example.Du_An_TTS_Test.Entity.Product;
import com.example.Du_An_TTS_Test.Repository.ProductElasticsearchRepository;
import com.example.Du_An_TTS_Test.Repository.ProductsRepo;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsElasticsearchSevice {

    @Autowired
    private ProductElasticsearchRepository elasticsearchRepository;

    @Autowired
    private ProductsRepo productsRepo;
    @Autowired
    private RedisTemplate redisTemplate;

    public Iterable<ProductDto> getProducts() {
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
            ProductDto product = objectMapper.readValue(logMessage, ProductDto.class);


            elasticsearchRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @KafkaListener(topics = "updateViewID")
    public ProductDto updateViewProduct(String id) {

        Product optional = productsRepo.findById(Integer.valueOf(id)).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));

        optional.setView(optional.getView().intValue() + 1);

        Product product = productsRepo.save(optional);
        if (product != null) {
            ProductDto productDto = elasticsearchRepository.findById(Integer.valueOf(id)).orElseThrow(()
                    -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));

            try {
                ProductDto existingProduct = productDto;
                existingProduct.setView(productDto.getView().intValue() + 1);

                List<CommentDto> commentDtos = product.getComments().stream()
                        .map(comment -> new CommentDto(comment.getUser().getUsername(),
                                comment.getProduct().getId(),
                                comment.getCommentText(),
                                comment.getCreatedAt().toString()))
                        .collect(Collectors.toList());


                existingProduct.setComments(commentDtos);

                redisTemplate.opsForValue().set("product:" + product.getId(), existingProduct);

                return elasticsearchRepository.save(existingProduct);
            } catch (NumberFormatException e) {
                System.err.println("Invalid view number: ");
                return null;
            }
        }
        return null;
    }

    @KafkaListener(topics = "updateProduct", groupId = "products-group")
    public void updateProduct(String logMessage) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDto product = objectMapper.readValue(logMessage, ProductDto.class);

            ProductDto existingProduct = elasticsearchRepository.findById(product.getId()).orElseThrow(()
                    -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));

            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setCreatedBy(existingProduct.getCreatedBy());
            existingProduct.setUpdatedAt(product.getUpdatedAt());
            existingProduct.setView(product.getView());

            redisTemplate.opsForValue().set("product:" + existingProduct.getId(), existingProduct);

            elasticsearchRepository.save(existingProduct);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
