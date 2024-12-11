package com.example.Du_An_TTS_Test.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.time.LocalDateTime;

@Document(indexName = "product")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsElasticsearch {
    @Id
    private Integer id;

    private String name;

    private Double price;

    private Integer stock_quantity;

    private String created_at;

    private String updated_at;

    private Integer created_by;

    private Integer view;

}
