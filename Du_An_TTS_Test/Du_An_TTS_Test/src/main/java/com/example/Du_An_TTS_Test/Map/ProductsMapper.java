package com.example.Du_An_TTS_Test.Map;

import com.example.Du_An_TTS_Test.Dto.ProductDto;
import com.example.Du_An_TTS_Test.Dto.UserDto;
import com.example.Du_An_TTS_Test.Entity.Product;
import com.example.Du_An_TTS_Test.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductsMapper {

    ProductsMapper PRODUCTS_MAPPER = Mappers.getMapper(ProductsMapper.class);

    void updateEntityFromDto(ProductDto productDto, @MappingTarget Product product);
    void updateProductFromEntity(Product product, @MappingTarget ProductDto productDto);

    User mapUserFromId(Integer userId);
    Product mapProductFromId(Integer productId);
    Integer mapUserToId(User user);
    Integer mapProductToId(Product product);
}
