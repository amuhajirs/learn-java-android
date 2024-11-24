package com.example.learn.data.api;

import com.example.learn.data.dto.resto.GetRestosDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestoApi {
    @GET("/api/v1/user/restaurants?")
    Call<GetRestosDto.Response> getRestos(
            @Query("search") String search,
            @Query("page") String page,
            @Query("limit") String limit,
            @Query("order") String order,
            @Query("direction") String direction,
            @Query("category_id") String categoryId
    );
}
