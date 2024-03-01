package com.backend.request;

import com.backend.model.Address;
import com.backend.model.ContactInformation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RestaurantRequest {
    private String name;

    private String description;

    private String cuisineType;

    private Address address;

    private ContactInformation contactInformation;

    private String openingHours;

    private List<String> images = new ArrayList<>();
}
