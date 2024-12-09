package com.example.learn.features.transactions.domain.usecase;

import com.example.learn.shared.data.dto.ErrorDto;
import com.example.learn.features.transactions.data.dto.CreateCartDto;
import com.example.learn.features.transactions.data.dto.CreateOrderDto;
import com.example.learn.features.transactions.domain.repository.TrxRepository;
import com.google.gson.Gson;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import retrofit2.Response;

public class CreateOrderUseCase {
    private final TrxRepository trxRepository;

    @Inject
    public CreateOrderUseCase(TrxRepository trxRepository) {
        this.trxRepository = trxRepository;
    }

    public CompletableFuture<CreateOrderDto.Response> executeAsync(CreateCartDto.Body[] body) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return execute(body);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CreateOrderDto.Response execute(CreateCartDto.Body[] body) throws Exception {
        int cartId = 0;

        try {
            for (CreateCartDto.Body b: body){
                Response<CreateCartDto.Response> res = trxRepository.createCart(b).execute();
                if(res.isSuccessful()) {
                    cartId = res.body().data.id;
                } else {
                    assert res.errorBody() != null;
                    String errorBody = res.errorBody().string();
                    ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                    throw new Exception(errorResponse.message);
                }
            }
        } catch (Throwable e) {
            throw new Exception(e.toString());
        }

        if(cartId == 0) {
            throw new Exception("Gagal menambahkan ke cart");
        }

        try {
            Response<CreateOrderDto.Response> res = trxRepository.createOrder(new CreateOrderDto.Body(cartId)).execute();
            if(res.isSuccessful()) {
                return res.body();
            } else {
                assert res.errorBody() != null;
                String errorBody = res.errorBody().string();
                ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                throw new Exception(errorResponse.message);
            }
        } catch (Throwable e) {
            throw new Exception(e.toString());
        }
    }
}
