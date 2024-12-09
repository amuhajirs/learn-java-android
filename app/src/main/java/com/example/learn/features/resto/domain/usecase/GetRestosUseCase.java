package com.example.learn.features.resto.domain.usecase;

import com.example.learn.features.resto.data.dto.GetRestosDto;
import com.example.learn.features.resto.domain.repository.RestoRepository;

import javax.inject.Inject;

import retrofit2.Callback;

public class GetRestosUseCase {
    private final RestoRepository restoRepository;

    @Inject
    public GetRestosUseCase(RestoRepository restoRepository) {
        this.restoRepository = restoRepository;
    }

    public void execute(GetRestosDto.Query query, Callback<GetRestosDto.Response> callback) {
        restoRepository.getRestos(query).enqueue(callback);
    }
}
