package com.example.Du_An_TTS_Test.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull(message = "username cannot be null")
    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    @NotEmpty(message = "Password must be 8 characters or more")
    @Min(value = 8, message = "Password must be 8 characters or more")
    String password;


    String email;

    @ManyToMany
    @NotNull(message = "roles cannot be null")
    Set<Role> roles;

    String created_at;

    String updated_at;
}
