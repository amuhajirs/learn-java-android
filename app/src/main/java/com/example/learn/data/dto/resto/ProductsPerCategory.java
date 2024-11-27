package com.example.learn.data.dto.resto;

import com.example.learn.data.dto.MetaPagination;
import com.example.learn.domain.model.Category;
import com.example.learn.domain.model.Product;

public class ProductsPerCategory {
    public Category category;
    public MetaPagination meta;
    public Product[] data;
}
