package com.example.Du_An_TTS_Test.Dto;

import com.example.Du_An_TTS_Test.Entity.Comment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.Document;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(indexName = "product")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ProductDto {
    @Id
    Integer id;

    String name;

    Double price;

    Integer quantity;

    String createdAt;

    String updatedAt;

    Integer createdBy;

    Number view;

    List<CommentDto> comments ;

}
