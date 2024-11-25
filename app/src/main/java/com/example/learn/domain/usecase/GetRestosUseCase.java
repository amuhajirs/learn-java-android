package com.example.learn.domain.usecase;

import com.example.learn.data.dto.resto.GetRestosDto;
import com.example.learn.data.repository.RestoRepositoryImpl;
import com.example.learn.domain.repository.RestoRepository;

import retrofit2.Call;

public class GetRestosUseCase {
    private final RestoRepository restoRepository;

    public GetRestosUseCase(RestoRepository restoRepository) {
        this.restoRepository = restoRepository;
    }

    public Call<GetRestosDto.Response> execute() {
        return restoRepository.getRestos();
    }
}
