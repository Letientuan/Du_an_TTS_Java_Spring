package com.example.Du_An_TTS_Test.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Comment  implements Serializable{
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @NotNull(message = "user cannot be null")
    @JoinColumn(name = "user_id", nullable=false)
    User user;

;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="product_id", nullable=false)
    Product product;

    @NotNull(message = "Comment text cannot be null")
    String commentText;

    LocalDateTime createdAt;

}
