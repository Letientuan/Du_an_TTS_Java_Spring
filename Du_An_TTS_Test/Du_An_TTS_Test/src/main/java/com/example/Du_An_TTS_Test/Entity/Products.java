package com.example.Du_An_TTS_Test.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Products implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "name cannot be null")
    private String name;

    @NotNull(message = "price cannot be null")
    private Double price;

    @NotNull(message = "stock_quantity cannot be null")
    private Integer stock_quantity;

    private String created_at;

    private String updated_at;

    private Integer created_by;

    private Number view;

}
