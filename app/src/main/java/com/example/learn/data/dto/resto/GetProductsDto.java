package com.example.learn.data.dto.resto;

public class GetProductsDto {
    static public class Response {
        public MetaGetRestos meta;
        public ProductsPerCategory[] data;
    }
}
