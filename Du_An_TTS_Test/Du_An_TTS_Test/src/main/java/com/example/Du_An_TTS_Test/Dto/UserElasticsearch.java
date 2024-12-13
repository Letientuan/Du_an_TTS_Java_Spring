package com.example.Du_An_TTS_Test.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.sql.Date;

@Document(indexName = "users")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserElasticsearch {
    @Id
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String role;

    private String created_at;

    private String updated_at;

}
