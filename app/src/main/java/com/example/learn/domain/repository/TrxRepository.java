package com.example.learn.domain.repository;

import com.example.learn.data.dto.trx.CreateCartDto;
import com.example.learn.data.dto.trx.CreateOrderDto;
import com.example.learn.data.dto.trx.GetTransactionDto;

import retrofit2.Call;

public interface TrxRepository {
    Call<GetTransactionDto.Response> getTransactions(String search);
    Call<CreateCartDto.Response> createCart(CreateCartDto.Body body);
    Call<CreateOrderDto.Response> createOrder(CreateOrderDto.Body body);
}
