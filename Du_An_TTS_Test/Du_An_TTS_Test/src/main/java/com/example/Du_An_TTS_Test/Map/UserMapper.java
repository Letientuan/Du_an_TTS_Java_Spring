package com.example.Du_An_TTS_Test.Map;


import com.example.Du_An_TTS_Test.Dto.UserElasticsearch;
import com.example.Du_An_TTS_Test.Entity.Users;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    void updateUserFromDto(UserElasticsearch userElasticsearch, @MappingTarget Users users);

    UserElasticsearch productsElasticsearch(Users products);
}
