package com.example.Du_An_TTS_Test.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "comments")
@Data
public class comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Users user_id;

    @ManyToOne
    private Products product_id;

    private String comment_text;

    private Date created_at;

}
