package com.example.learn.data.repository;

import com.example.learn.data.api.RestoApi;
import com.example.learn.data.api.RetrofitClient;
import com.example.learn.data.dto.resto.GetRestosDto;

import retrofit2.Call;

public class RestoRepository {
    private final RestoApi restoApi;

    public RestoRepository() {
        restoApi = RetrofitClient.getClient().create(RestoApi.class);
    }

    public Call<GetRestosDto.Response> getRestos() { return restoApi.getRestos("","1", "10", "id", "desc", ""); }
}
