package com.example.Du_An_TTS_Test.Dto.Repon;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRepon {
    Integer id;

    String username;

    String password;

    String email;

    Set<String> role;

    Date created_at;

    Date updated_at;
}
