package com.backend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Embeddable
public class RestaurantDTO {
    private Long id;

    private String name;

    @Column(length = 1000)
    private List<String> images = new ArrayList<>();

    private String description;
}
