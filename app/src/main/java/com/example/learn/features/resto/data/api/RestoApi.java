package com.example.learn.features.resto.data.api;

import com.example.learn.features.resto.data.dto.GetProductsDto;
import com.example.learn.features.resto.data.dto.GetRestosDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("/api/v1/user/restaurants/{id}/products?")
    Call<GetProductsDto.Response> getProducts(
            @Path("id") int restaurantId,
            @Query("search") String search,
            @Query("page") String page,
            @Query("limit") String limit,
            @Query("order") String order,
            @Query("direction") String direction,
            @Query("category_id") String categoryId
    );
}
