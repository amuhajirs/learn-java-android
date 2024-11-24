package com.example.learn.data.dto.resto;

import com.example.learn.data.dto.MetaPagination;
import com.example.learn.domain.model.Category;
import com.example.learn.domain.model.Restaurant;

public class RestaurantsPerCategory {
    public Category category;
    public MetaPagination meta;
    public Restaurant[] data;
}
