package com.example.learn.features.resto.data.dto;

import com.example.learn.shared.data.dto.MetaPagination;

public class RestaurantsPerCategory {
    public CategoryDto category;
    public MetaPagination meta;
    public RestaurantDto[] data;
}
