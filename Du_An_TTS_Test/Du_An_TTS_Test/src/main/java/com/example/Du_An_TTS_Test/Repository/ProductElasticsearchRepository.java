package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Dto.ProductDto;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDto, Integer> {
}

