package com.example.Du_An_TTS_Test.Map;


import com.example.Du_An_TTS_Test.Dto.UserDto;
import com.example.Du_An_TTS_Test.Entity.User;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

public interface UserMapper {


    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    void updateUserFromDto(UserDto userDto, @MappingTarget User user);
    void updateDtoFromUser(User user, @MappingTarget  UserDto userDto);

}
