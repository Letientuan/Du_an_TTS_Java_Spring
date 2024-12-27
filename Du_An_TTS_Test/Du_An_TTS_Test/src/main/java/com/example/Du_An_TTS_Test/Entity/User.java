package com.example.Du_An_TTS_Test.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;


@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull(message = "username cannot be null")
    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    @NotNull(message = "password cannot be null")
    String password;

    String email;

    @ManyToMany
    @NotNull(message = "roles cannot be null")
    Set<Role> roles;


    LocalDateTime createdAt;


    LocalDateTime updatedAt;


}
