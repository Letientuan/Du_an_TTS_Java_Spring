package com.example.Du_An_TTS_Test.Dto;

import com.example.Du_An_TTS_Test.Entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserDto {

    @Id
    Integer id;

    String username;

    String password;

    String email;

    Set<Role> roles;

    String createdAt;

    String updatedAt;


    public UserDto(String userId) {
    }
}
