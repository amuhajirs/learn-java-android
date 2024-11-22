package com.example.test.data.repository;

import com.example.test.data.api.RestoApi;
import com.example.test.data.api.RetrofitClient;
import com.example.test.data.dto.resto.GetRestosDto;

import retrofit2.Call;

public class RestoRepository {
    private final RestoApi restoApi;

    public RestoRepository() {
        restoApi = RetrofitClient.getClient().create(RestoApi.class);
    }

    public Call<GetRestosDto.Response> getRestos() { return restoApi.getRestos("","1", "10", "id", "desc", ""); }
}
