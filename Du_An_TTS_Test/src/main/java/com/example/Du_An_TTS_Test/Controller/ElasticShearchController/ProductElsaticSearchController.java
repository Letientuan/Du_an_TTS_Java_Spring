package com.example.Du_An_TTS_Test.Controller.ElasticShearchController;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice.ElastcSearchSevice;
import com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice.ProductsElasticsearchSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ElasticSearch/Product")
public class ProductElsaticSearchController {

    @Autowired
    private ProductsElasticsearchSevice productsElasticsearchSevice;

    @Autowired
    private ElastcSearchSevice elastcSearchSevice;

    @GetMapping("getAll")
    public ResponseEntity<?> getAllProducElastic() {
        Iterable<ProductsElasticsearch> products = productsElasticsearchSevice.getProducts();

        return ResponseEntity.ok(products);
    }

//    @PostMapping("add")
//    public ResponseEntity<?> addProductElastic(@RequestBody ProductsElasticsearch productsElasticsearch) {
//        return ResponseEntity.ok(productsElasticsearchSevice.insertProduct(productsElasticsearch));
//    }
//
//    @PostMapping("update/{id}")
//    public ResponseEntity<?> updateProductElastic(
//            @RequestBody ProductsElasticsearch productsElasticsearch,
//            @PathVariable Integer id) {
//        return ResponseEntity.ok(productsElasticsearchSevice.updateProduct(productsElasticsearch,id));
//    }
//    @DeleteMapping("delete/{id}")
//    public ResponseEntity<?> updateProductElastic(@PathVariable Integer id){
//        productsElasticsearchSevice.deleteProduct(id);
//        return ResponseEntity.ok(id);
//    }

    @GetMapping("matchAll")
    public SearchResponse<Map> matchAll() throws IOException {
        SearchResponse<Map> mapSearchResponse = elastcSearchSevice.matchAllSevice();
        System.out.println("search" + mapSearchResponse.hits().hits().toString());
        return mapSearchResponse;

    }

    @GetMapping("matchAllProduct")
    public List<ProductsElasticsearch> matchAllProduct() throws IOException {
        SearchResponse<ProductsElasticsearch> mapSearchResponse = elastcSearchSevice.matchAllSeviceProduct();

        List<Hit<ProductsElasticsearch>> hitList = mapSearchResponse.hits().hits();
        List<ProductsElasticsearch> list = new ArrayList<>();
        for (Hit<ProductsElasticsearch> hit : hitList
        ) {
            list.add(hit.source());
        }
        return list;

    }

    @GetMapping("matchAllProduct/{name}")
    public List<ProductsElasticsearch> matchAllProductFiledName(@PathVariable String name) throws IOException {

        SearchResponse<ProductsElasticsearch> mapSearchResponse = elastcSearchSevice.fieldNameSeviceProduct(name);

        List<Hit<ProductsElasticsearch>> hitList = mapSearchResponse.hits().hits();
        List<ProductsElasticsearch> list = new ArrayList<>();
        for (Hit<ProductsElasticsearch> hit : hitList
        ) {
            list.add(hit.source());
        }
        return list;

    }
}
