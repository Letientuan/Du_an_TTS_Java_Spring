package com.example.Du_An_TTS_Test.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "comments")
@Data
@Getter
@Setter
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull(message = "user cannot be null")
    private Users user_id;

    @ManyToOne
    @JsonIgnore
    private Products product;

    @NotNull(message = "Comment text cannot be null")
    private String comment_text;

    private Date created_at;

}
