package com.example.Du_An_TTS_Test.Controller;

import com.cloudinary.Cloudinary;
import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Sevice.ProductsSevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.Du_An_TTS_Test.Entity.Products;


import java.io.IOException;


@Controller
@RequestMapping("Admin/products")
public class productsController {

    @Autowired
    private ProductsSevice productsSevice;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Cloudinary cloudinary;

    private static final String TOPIC = "updateViewID";

    @GetMapping("redix/ProductDetail/{id}")
    public ResponseEntity<?> getbyidProductredix(@PathVariable Integer id) {
        Products products = productsSevice.findbyidProduct(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("ProductDetail/{id}")
    public ResponseEntity<?> getbyidProduct(@PathVariable Integer id) throws JsonProcessingException {
        Products products = productsSevice.updateView(id);
        String logMessage = objectMapper.writeValueAsString(products);
        kafkaTemplate.send(TOPIC, logMessage);

        return ResponseEntity.ok(products);
    }

    @PostMapping("add/Elasticsearch")
    public ResponseEntity<?> addProductElasticsearch(@Valid @RequestBody Products products1)
            throws IOException {
//
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        String imageUrl = (String) uploadResult.get("secure_url");

        products1.setView(1);
        Products products = productsSevice.addProduct(products1);
        String logMessage = objectMapper.writeValueAsString(products);
        kafkaTemplate.send("addproduct", logMessage);

        return ResponseEntity.ok(products1);
    }


    @PutMapping("Elasticsearch/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductsElasticsearch product) {
        product.setId(id);
        try {
            Products products = productsSevice.updateProduct(id, product);
            if (products != null) {

                String logMessage = objectMapper.writeValueAsString(products);
                kafkaTemplate.send("updateProduct", logMessage);
                return ResponseEntity.ok(products);
            } else {
                return new ResponseEntity<>("upDate thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("upDate thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("Elasticsearch/deleteProduct/{id}")
    public ResponseEntity<?> deleteProductElasticsearch(@PathVariable Integer id) {
        Products products = productsSevice.findbyidProducts(id);
        if (products != null) {
            productsSevice.deleteProduct(id);
            String logMessage = "" + id;
            kafkaTemplate.send("deleteProduct", logMessage);
            return ResponseEntity.ok("delete Thành công");
        } else {
            return ResponseEntity.ok("thất bại");
        }

    }

}
