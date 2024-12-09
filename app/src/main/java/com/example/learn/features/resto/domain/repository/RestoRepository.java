package com.example.learn.features.resto.domain.repository;

import com.example.learn.features.resto.data.dto.GetProductsDto;
import com.example.learn.features.resto.data.dto.GetRestosDto;

import retrofit2.Call;

public interface RestoRepository {
    Call<GetRestosDto.Response> getRestos(GetRestosDto.Query query);
    Call<GetProductsDto.Response> getProducts(int restaurantId, GetProductsDto.Query query);
}
