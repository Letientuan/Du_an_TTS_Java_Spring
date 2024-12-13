package com.example.Du_An_TTS_Test.Dto;

import com.example.Du_An_TTS_Test.Entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.sql.Date;
import java.util.Set;

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

    @ManyToMany
    @NotNull(message = "roles cannot be null")
    private Set<Role> roles;

    private String created_at;

    private String updated_at;

}
