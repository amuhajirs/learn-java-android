package com.example.test.data.dto.resto;

public class GetRestosDto {
    static public class Response {
        public MetaGetRestos meta;
        public RestaurantsPerCategory[] data;
    }
}
