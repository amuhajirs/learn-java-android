package com.example.learn.data.repository;

import com.example.learn.data.api.TrxApi;
import com.example.learn.data.dto.trx.CreateCartDto;
import com.example.learn.data.dto.trx.CreateOrderDto;
import com.example.learn.data.dto.trx.GetTransactionDto;
import com.example.learn.domain.repository.TrxRepository;

import retrofit2.Call;

public class TrxRepositoryImpl implements TrxRepository {
    private final TrxApi trxApi;

    public TrxRepositoryImpl(TrxApi trxApi) {
        this.trxApi = trxApi;
    }

    public Call<GetTransactionDto.Response> getTransactions(String search) {
        return trxApi.getTransactions(search, "1", "10", "created_at", "desc", "");
    };
    public Call<CreateCartDto.Response> createCart(CreateCartDto.Body body) {
        return trxApi.createCart(body);
    };

    public Call<CreateOrderDto.Response> createOrder(CreateOrderDto.Body body) {
        return trxApi.createOrder(body);
    };
}
