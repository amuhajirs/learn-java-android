package com.example.learn.features.transactions.domain.usecase;

import com.example.learn.features.transactions.data.dto.GetTransactionDto;
import com.example.learn.features.transactions.domain.repository.TrxRepository;

import javax.inject.Inject;

import retrofit2.Callback;

public class GetTransactionsUseCase {
    private final TrxRepository trxRepository;

    @Inject
    public GetTransactionsUseCase(TrxRepository trxRepository) {
        this.trxRepository = trxRepository;
    }

    public void execute(String search, Callback<GetTransactionDto.Response> callback) {
        trxRepository.getTransactions(search).enqueue(callback);
    }
}
