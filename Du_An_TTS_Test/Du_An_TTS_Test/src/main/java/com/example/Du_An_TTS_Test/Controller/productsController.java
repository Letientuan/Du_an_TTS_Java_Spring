package com.example.Du_An_TTS_Test.Controller;

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


@Controller
@RequestMapping("Admin/products")
public class productsController {

    @Autowired
    private ProductsSevice productsSevice;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    private static final String TOPIC = "updateViewID";

    @GetMapping("redix/ProductDetail/{id}")
    public ResponseEntity<?> getbyidProductredix(@PathVariable Integer id) {
        Products products = productsSevice.findbyidProduct(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("ProductDetail/{id}")
    public ResponseEntity<?> getbyidProduct(@PathVariable Integer id) throws JsonProcessingException {
        Products products = productsSevice.Updateview(id);
        String logMessage = objectMapper.writeValueAsString(products);
        kafkaTemplate.send(TOPIC, logMessage);

        return ResponseEntity.ok(products);
    }

    @PostMapping("add/Elasticsearch")
    public ResponseEntity<?> addProductElasticsearch(@Valid @RequestBody  Products products1) throws JsonProcessingException {
        products1.setView(1);
        Products products = productsSevice.addProduct(products1);
        String logMessage = objectMapper.writeValueAsString(products);
        kafkaTemplate.send("addproduct", logMessage);

        return ResponseEntity.ok(products1);
    }


    @PutMapping("Elasticsearch/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody Products product) {
        try {

            product.setId(id);
            productsSevice.updateProduct(id, product);
            String logMessage = objectMapper.writeValueAsString(product);
            kafkaTemplate.send("updateProduct", logMessage);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("update thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("Elasticsearch/deleteProduct/{id}")
    public ResponseEntity<?> deleteProductElasticsearch(@PathVariable Integer id) {
//        Products products = productsSevice.findbyidProducts(id);
        productsSevice.deleteProduct(id);
        String logMessage = "" + id;
        kafkaTemplate.send("deleteProduct", logMessage);
        return ResponseEntity.ok("delete Thành công");
    }

}
