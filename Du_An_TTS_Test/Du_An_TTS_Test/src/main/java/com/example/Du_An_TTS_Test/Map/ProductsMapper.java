package com.example.Du_An_TTS_Test.Map;

import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductsMapper {

    ProductsMapper PRODUCTS_MAPPER = Mappers.getMapper(ProductsMapper.class);

    void updateEntityFromDto(ProductsElasticsearch productsElasticsearch, @MappingTarget Products products);

    ProductsElasticsearch productsElasticsearch(Products products);

    Products productMapperToProduct(ProductsElasticsearch productDto);
}
