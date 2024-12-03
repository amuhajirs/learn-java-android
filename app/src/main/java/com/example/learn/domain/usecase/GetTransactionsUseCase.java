package com.example.learn.domain.usecase;

import com.example.learn.data.dto.trx.GetTransactionDto;
import com.example.learn.domain.repository.TrxRepository;

import javax.inject.Inject;

import retrofit2.Callback;

public class GetTransactionsUseCase {
    private final TrxRepository trxRepository;

    @Inject
    public GetTransactionsUseCase(TrxRepository trxRepository) {
        this.trxRepository = trxRepository;
    }

    public void execute(Callback<GetTransactionDto.Response> callback) {
        trxRepository.getTransactions().enqueue(callback);
    }
}
