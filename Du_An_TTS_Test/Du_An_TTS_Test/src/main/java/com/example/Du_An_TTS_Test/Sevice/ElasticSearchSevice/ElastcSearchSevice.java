package com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;

import com.example.Du_An_TTS_Test.Dto.ProductDto;
import com.example.Du_An_TTS_Test.Util.ElastcSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Supplier;

import java.util.Map;

@Service
public class ElastcSearchSevice {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public SearchResponse<Map> matchAllSevice() throws IOException {
        Supplier<Query> supplier = ElastcSearchUtil.supplier();
        SearchResponse<Map> mapSearchResponse = elasticsearchClient.search(s -> s.query(supplier.get()), Map.class);
        System.out.println(":" + supplier.get().toString());

        return mapSearchResponse;
    }

    public SearchResponse<ProductDto> matchAllSeviceProduct() throws IOException {
        Supplier<Query> supplier = ElastcSearchUtil.supplier();
        SearchResponse<ProductDto> mapSearchResponse = elasticsearchClient.search(s -> s.index("product").query(supplier.get()), ProductDto.class);
        System.out.println(":" + supplier.get().toString());

        return mapSearchResponse;
    }

    public SearchResponse<ProductDto> fieldNameSeviceProduct(String name) throws IOException {
        Supplier<Query> supplier = ElastcSearchUtil.suppliername(name);
        SearchResponse<ProductDto> mapSearchResponse = elasticsearchClient.search(s -> s.index("product").query(supplier.get()), ProductDto.class);
        System.out.println(":" + supplier.get().toString());

        return mapSearchResponse;
    }
}
