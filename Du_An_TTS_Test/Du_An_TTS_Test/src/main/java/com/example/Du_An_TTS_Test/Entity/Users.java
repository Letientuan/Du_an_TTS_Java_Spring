package com.example.Du_An_TTS_Test.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.sql.Date;
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

    @Size( min = 6,max = 15)
    @NotNull(message = "username cannot be null")
    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    @NotNull(message = "password cannot be null")
    @Size( min = 6)
    String password;

    @NotNull(message = "email cannot be null")
    String email;

    @ManyToMany
    @NotNull(message = "roles cannot be null")
    Set<Role> roles;

    String created_at;

    String updated_at;
}
