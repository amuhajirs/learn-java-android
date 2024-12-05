package com.example.learn.presentation.ui.transaction;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learn.data.dto.ErrorDto;
import com.example.learn.data.dto.trx.GetTransactionDto;
import com.example.learn.domain.model.FilterTransactions;
import com.example.learn.domain.usecase.GetTransactionsUseCase;
import com.example.learn.helper.utils.Resource;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class TrxViewModel extends ViewModel {
    private final GetTransactionsUseCase getTransactionsUseCase;
    private final MutableLiveData<Resource<GetTransactionDto.Response>> transactionsState = new MutableLiveData<>();
    private final MutableLiveData<FilterTransactions> filterTrx = new MutableLiveData<>(new FilterTransactions());

    @Inject
    public TrxViewModel(GetTransactionsUseCase getTransactionsUseCase) {
        this.getTransactionsUseCase = getTransactionsUseCase;
    }

    public LiveData<Resource<GetTransactionDto.Response>> getTransactionsState() {
        if(transactionsState.getValue() == null) {
            fetchGetTransactions();
        }

        return transactionsState;
    }

    public LiveData<FilterTransactions> getFilterTrx() {
        return filterTrx;
    }

    public void updateSearchFilter(String value) {
        FilterTransactions copyFilter = filterTrx.getValue().copy();
        copyFilter.search = value;
        filterTrx.setValue(copyFilter);
    }

    public void updateStatusFilter(String value) {
        FilterTransactions copyFilter = filterTrx.getValue().copy();
        copyFilter.status = value;
        filterTrx.setValue(copyFilter);
    }

    public void updateCategoryFilter(String value) {
        FilterTransactions copyFilter = filterTrx.getValue().copy();
        copyFilter.categoryId = value;
        filterTrx.setValue(copyFilter);
    }

    public void updateDateFilter(String value) {
        FilterTransactions copyFilter = filterTrx.getValue().copy();
        copyFilter.date = value;
        filterTrx.setValue(copyFilter);
    }

    public void clearFilter() {
        filterTrx.setValue(new FilterTransactions());
    }

    public void fetchGetTransactions() {
        transactionsState.setValue(Resource.loading());

        getTransactionsUseCase.execute(filterTrx.getValue().search, new Callback<GetTransactionDto.Response>() {
            @Override
            public void onResponse(Call<GetTransactionDto.Response> call, Response<GetTransactionDto.Response> response) {
                if(response.isSuccessful()) {
                    transactionsState.postValue(Resource.success(response.body()));
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                        transactionsState.postValue(Resource.error(errorResponse.message));
                    } catch (Throwable e) {
                        Log.e("UNKNOWN GET TRANSACTIONS ERROR", e.toString());
                        transactionsState.postValue(Resource.error(e.toString()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetTransactionDto.Response> call, Throwable t) {
                Log.e("UNKNOWN GET TRANSACTIONS ERROR", t.toString());
                transactionsState.postValue(Resource.error(t.toString()));
            }
        });
    }
}
