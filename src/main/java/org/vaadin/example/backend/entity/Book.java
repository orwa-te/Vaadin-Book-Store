package org.vaadin.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotNull(message = "Publication year is mandatory")
    @Min(value = 0, message = "Year must be positive")
    @Max(value = 2030, message = "Year cannot me more than 2030")
    private Integer publicationYear;

    @NotBlank(message = "Description is mandatory")
    @Column(length = 2000)
    private String description;

}