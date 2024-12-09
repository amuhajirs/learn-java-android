package com.example.learn.features.transactions.data.api;

import com.example.learn.features.transactions.data.dto.CreateCartDto;
import com.example.learn.features.transactions.data.dto.CreateOrderDto;
import com.example.learn.features.transactions.data.dto.GetTransactionDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TrxApi {
    @GET("/api/v1/user/transactions/orders?")
    Call<GetTransactionDto.Response> getTransactions(
        @Query("search") String search,
        @Query("page") String page,
        @Query("limit") String limit,
        @Query("order") String order,
        @Query("direction") String direction,
        @Query("filter") String filter
    );

    @POST("/api/v1/user/transactions/carts")
    Call<CreateCartDto.Response> createCart(@Body CreateCartDto.Body body);

    @POST("/api/v1/user/transactions/orders")
    Call<CreateOrderDto.Response> createOrder(@Body CreateOrderDto.Body body);
}
