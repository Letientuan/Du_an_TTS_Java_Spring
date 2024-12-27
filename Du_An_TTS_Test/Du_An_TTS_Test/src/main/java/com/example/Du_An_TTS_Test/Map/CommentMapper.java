package com.example.Du_An_TTS_Test.Map;

import com.example.Du_An_TTS_Test.Dto.CommentDto;
import com.example.Du_An_TTS_Test.Entity.Comment;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

public interface CommentMapper {

    CommentMapper COMMEN_MAPPER = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "commentText", ignore = true)
    void updateFromDtotoEntity(Comment comment, @MappingTarget CommentDto commentDto);

    void updateEntityFromDto(CommentDto commentDto, @MappingTarget Comment comment);

}
