package com.example.test.data.dto.resto;

import com.example.test.data.dto.MetaPagination;
import com.example.test.domain.model.Category;
import com.example.test.domain.model.Restaurant;

public class RestaurantsPerCategory {
    public Category category;
    public MetaPagination meta;
    public Restaurant[] data;
}
