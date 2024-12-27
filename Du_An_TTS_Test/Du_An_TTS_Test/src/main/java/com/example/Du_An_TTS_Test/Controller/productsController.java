package com.example.Du_An_TTS_Test.Controller;


import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.Du_An_TTS_Test.Dto.ProductDto;
import com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice.ElastcSearchSevice;
import com.example.Du_An_TTS_Test.Sevice.ProductsSevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Controller
@Slf4j
@RequestMapping("Admin/products")
public class ProductsController {

    @Autowired
    private ProductsSevice productsSevice;

    @Autowired
    private ElastcSearchSevice elastcSearchSevice;

    @GetMapping("productdetail/{id}")
    public ResponseEntity<?> getbyidProduct(@PathVariable Integer id) throws JsonProcessingException {

        return ResponseEntity.ok(productsSevice.findbyid(id));
    }

    @PostMapping("add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDto product) {

        ProductDto newProduct = productsSevice.addProduct(product);

        return ResponseEntity.ok(newProduct);
    }


    @PutMapping("update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDto productdto) throws JsonProcessingException {
        ProductDto products = productsSevice.updateProduct(id, productdto);
        if (products != null) {
            return ResponseEntity.ok(products);
        } else {
            return new ResponseEntity<>("upDate thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {

        productsSevice.deleteProduct(id);

        return ResponseEntity.ok("delete Thành công");

    }

    @GetMapping("matchAll")
    public SearchResponse<Map> matchAll() throws IOException {
        SearchResponse<Map> mapSearchResponse = elastcSearchSevice.matchAllSevice();
        System.out.println("search" + mapSearchResponse.hits().hits().toString());
        return mapSearchResponse;

    }

    @GetMapping("matchAllProduct")
    public List<ProductDto> matchAllProduct() throws IOException {
        SearchResponse<ProductDto> mapSearchResponse = elastcSearchSevice.matchAllSeviceProduct();

        List<Hit<ProductDto>> hitList = mapSearchResponse.hits().hits();
        List<ProductDto> list = new ArrayList<>();
        for (Hit<ProductDto> hit : hitList
        ) {
            list.add(hit.source());
        }
        return list;

    }

    @GetMapping("matchAllProduct/{name}")
    public List<ProductDto> matchAllProductFiledName(@PathVariable String name) throws IOException {

        SearchResponse<ProductDto> mapSearchResponse = elastcSearchSevice.fieldNameSeviceProduct(name);

        List<Hit<ProductDto>> hitList = mapSearchResponse.hits().hits();
        List<ProductDto> list = new ArrayList<>();
        for (Hit<ProductDto> hit : hitList
        ) {
            list.add(hit.source());
        }
        return list;

    }
}
