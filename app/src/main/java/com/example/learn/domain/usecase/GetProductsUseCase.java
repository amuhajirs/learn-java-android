package com.example.learn.domain.usecase;

import com.example.learn.data.dto.resto.GetProductsDto;
import com.example.learn.domain.repository.RestoRepository;

import javax.inject.Inject;

import retrofit2.Callback;

public class GetProductsUseCase {
    private final RestoRepository restoRepository;

    @Inject
    public GetProductsUseCase(RestoRepository restoRepository) {
        this.restoRepository = restoRepository;
    }

    public void execute(int restaurantId, Callback<GetProductsDto.Response> callback) {
        restoRepository.getProducts(restaurantId).enqueue(callback);
    }
}
