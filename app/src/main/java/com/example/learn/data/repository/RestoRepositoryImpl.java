package com.example.learn.data.repository;

import com.example.learn.data.api.RestoApi;
import com.example.learn.data.dto.resto.GetRestosDto;
import com.example.learn.domain.repository.RestoRepository;

import retrofit2.Call;

public class RestoRepositoryImpl implements RestoRepository {
    private final RestoApi restoApi;

    public RestoRepositoryImpl(RestoApi restoApi) {
        this.restoApi = restoApi;
    }

    public Call<GetRestosDto.Response> getRestos() {
        return restoApi.getRestos("", "1", "10", "id", "desc", "");
    }
}
