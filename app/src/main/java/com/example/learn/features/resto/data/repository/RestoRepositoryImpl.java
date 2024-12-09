package com.example.learn.features.resto.data.repository;

import com.example.learn.features.resto.data.api.RestoApi;
import com.example.learn.features.resto.data.dto.GetProductsDto;
import com.example.learn.features.resto.data.dto.GetRestosDto;
import com.example.learn.features.resto.domain.repository.RestoRepository;

import retrofit2.Call;

public class RestoRepositoryImpl implements RestoRepository {
    private final RestoApi restoApi;

    public RestoRepositoryImpl(RestoApi restoApi) {
        this.restoApi = restoApi;
    }

    public Call<GetRestosDto.Response> getRestos(GetRestosDto.Query query) {
        return restoApi.getRestos(query.search, query.page, query.limit, query.order, query.direction, query.categoryId);
    }

    public Call<GetProductsDto.Response> getProducts(int restaurantId, GetProductsDto.Query query) {
        return restoApi.getProducts(restaurantId, query.search, query.page, query.limit, query.order, query.direction, query.categoryId);
    }
}
