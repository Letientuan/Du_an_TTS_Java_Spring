package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Dto.CommentDto;
import com.example.Du_An_TTS_Test.Dto.ProductDto;
import com.example.Du_An_TTS_Test.Entity.Product;
import com.example.Du_An_TTS_Test.Map.ProductsMapper;
import com.example.Du_An_TTS_Test.Repository.CommentRepo;
import com.example.Du_An_TTS_Test.Repository.ProductsRepo;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsSevice {

    @Autowired
    private ProductsRepo productsRepo;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    LocalDateTime currentDateTime = LocalDateTime.now();

    private static final String TOPIC = "updateViewID";


    public Product findbyidProduct(Integer id) {


        Object cachedProductObject = redisTemplate.opsForValue().get("product:" + id);

        if (cachedProductObject != null) {
            ProductDto cachedProduct = (ProductDto) cachedProductObject;
            Product product = new Product();
            ProductsMapper.PRODUCTS_MAPPER.updateEntityFromDto(cachedProduct, product);
            return product;
        }
        Product product = productsRepo.findById(id).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
        try {

            ProductDto productDto = new ProductDto();
            ProductsMapper.PRODUCTS_MAPPER.updateProductFromEntity(product, productDto);

            List<CommentDto> commentDtos = product.getComments().stream()
                    .map(comment -> new CommentDto(comment.getUser().getUsername(),
                            comment.getProduct().getId(),
                            comment.getCommentText(),
                            comment.getCreatedAt().toString()))
                    .collect(Collectors.toList());
            productDto.setComments(commentDtos);

            redisTemplate.opsForValue().set("product:" + id, productDto);
            ProductsMapper.PRODUCTS_MAPPER.updateEntityFromDto(productDto, product);
            return product;
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public ProductDto findbyid(Integer id) throws JsonProcessingException {
        findbyidProduct(id);
        try {
            Object cachedProductObject = redisTemplate.opsForValue().get("product:" + id);
            ProductDto cachedProduct = (ProductDto) cachedProductObject;
            updateView(id);
            return cachedProduct;
        } catch (RuntimeException exception) {
            return null;
        }

    }

    public ProductDto addProduct(ProductDto product) {
        Product productFindbyName = productsRepo.findByName(product.getName());
        if (productFindbyName != null) {
            throw new RuntimeException(ErrorCode.INVALID_NAME_PRODUCT.getMessage());
        }
        try {
            Product newProduct = new Product();

            ProductsMapper.PRODUCTS_MAPPER.updateEntityFromDto(product, newProduct);

            newProduct.setView(1);
            newProduct.setCreatedAt(currentDateTime);

            Product savedProduct = productsRepo.save(newProduct);

            if (savedProduct != null) {

                product.setCreatedAt(currentDateTime.toString());
                product.setView(1);
                product.setId(savedProduct.getId());

                ProductsMapper.PRODUCTS_MAPPER.updateProductFromEntity(savedProduct, product);


                String logMessage = objectMapper.writeValueAsString(product);

                kafkaTemplate.send("addproduct", logMessage);

                redisTemplate.opsForValue().set("product:" + savedProduct.getId(), product);
            }
            return product;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return null;
        }

    }


    public void deleteProduct(Integer id) {
        Product product = findbyidProduct(id);
        try {
            productsRepo.deleteById(product.getId());
            String logMessage = "" + id;
            kafkaTemplate.send("deleteProduct", logMessage);

            redisTemplate.delete("product:" + id);

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void updateView(Integer id) throws JsonProcessingException {
        String logMessage = objectMapper.writeValueAsString(id);
        kafkaTemplate.send(TOPIC, logMessage);
    }


    public ProductDto updateProduct(Integer id, ProductDto productdto) {

        try {
            Product optional = findbyidProduct(id);
            productdto.setId(id);
            productdto.setView(optional.getView());
            productdto.setCreatedAt(optional.getCreatedAt().toString());

            ProductsMapper.PRODUCTS_MAPPER.updateEntityFromDto(productdto, optional);

            optional.setUpdatedAt(currentDateTime);


            Product newProduct = productsRepo.save(optional);

            if (newProduct != null) {

                productdto.setUpdatedAt(currentDateTime.toString());
                String logMessage = objectMapper.writeValueAsString(productdto);

                kafkaTemplate.send("updateProduct", logMessage);

                return productdto;

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }


}
