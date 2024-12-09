package com.example.learn.features.resto.data.dto;

import com.example.learn.shared.data.dto.MetaPagination;

public class ProductsPerCategory {
    public CategoryDto category;
    public MetaPagination meta;
    public ProductDto[] data;
}
