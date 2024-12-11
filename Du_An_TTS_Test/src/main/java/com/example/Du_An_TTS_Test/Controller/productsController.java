package com.example.Du_An_TTS_Test.Controller;

import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Sevice.ProductsSevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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


    private static final String TOPIC = "ID";

    @GetMapping("redix/ProductDetail/{id}")
    public ResponseEntity<?> getbyidProductredix(@PathVariable Integer id) {
        Products products = productsSevice.findbyidProduct(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("ProductDetail/{id}")
    public ResponseEntity<?> getbyidProduct(@PathVariable Integer id) {
        Products products = productsSevice.findbyidProducts(id);
        Integer ID = products.getId();
        kafkaTemplate.send(TOPIC, "" + ID);
        return ResponseEntity.ok(products);
    }

    @PostMapping("add/Elasticsearch")
    public ResponseEntity<?> addProductElasticsearch(@RequestBody ProductsElasticsearch products1) throws JsonProcessingException {

        Products products = new Products();
        products.setCreated_by(products1.getCreated_by());
        products.setStock_quantity(products1.getStock_quantity());
        products.setPrice(products1.getPrice());
        products.setName(products1.getName());
        products.setView(0);
        products.setCreated_at(products1.getCreated_at());

        Products products2 = productsSevice.addProduct(products);

        if (products2 == null || products2.getId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add product. ID was not assigned.");
        }


        products1.setId(products2.getId());

        String logMessage = objectMapper.writeValueAsString(products1);
        kafkaTemplate.send("addproduct", logMessage);

        return ResponseEntity.ok("Add thành công");
    }


    @PutMapping("Elasticsearch/updateProduct/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestBody Products product) {
        try {

            product.setId(id);
            productsSevice.updateProduct(id, product);
            String logMessage = objectMapper.writeValueAsString(product);
            kafkaTemplate.send("updateProduct", logMessage);
            return new ResponseEntity<>("update thành công", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("update thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("Elasticsearch/deleteProduct/{id}")
    public ResponseEntity<?> deleteProductElasticsearch(@PathVariable Integer id) {
        productsSevice.deleteProduct(id);
        String logMessage = "" + id;
        kafkaTemplate.send("deleteProduct", logMessage);
        return ResponseEntity.ok("delete Thành công");
    }

}
