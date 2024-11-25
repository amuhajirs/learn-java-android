package com.example.learn.domain.repository;

import com.example.learn.data.dto.resto.GetRestosDto;

import retrofit2.Call;

public interface RestoRepository {
    Call<GetRestosDto.Response> getRestos();
}
