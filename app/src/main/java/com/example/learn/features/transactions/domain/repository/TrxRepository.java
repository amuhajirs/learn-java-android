package com.example.learn.features.transactions.domain.repository;

import com.example.learn.features.transactions.data.dto.CreateCartDto;
import com.example.learn.features.transactions.data.dto.CreateOrderDto;
import com.example.learn.features.transactions.data.dto.GetTransactionDto;

import retrofit2.Call;

public interface TrxRepository {
    Call<GetTransactionDto.Response> getTransactions(String search);
    Call<CreateCartDto.Response> createCart(CreateCartDto.Body body);
    Call<CreateOrderDto.Response> createOrder(CreateOrderDto.Body body);
}
