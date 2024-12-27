package com.example.Du_An_TTS_Test.Entity;

import java.util.Set;
import java.util.stream.Stream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @NotNull(message = "name cannot be null")
    String name;

    @NotNull(message = "description cannot be null")
    String description;

    @ManyToMany(cascade = CascadeType.PERSIST)
    Set<Permission> permissions;

}
