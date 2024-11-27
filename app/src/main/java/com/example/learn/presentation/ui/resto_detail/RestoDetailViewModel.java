package com.example.learn.presentation.ui.resto_detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.learn.data.dto.ErrorDto;
import com.example.learn.data.dto.resto.GetProductsDto;
import com.example.learn.domain.usecase.GetProductsUseCase;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class RestoDetailViewModel extends ViewModel {
    private final GetProductsUseCase getProductsUseCase;
    private final int restaurantId;

    private final MutableLiveData<GetProductsDto.Response> products = new MutableLiveData<>();
    private final MutableLiveData<String> errorGetProductsMsg = new MutableLiveData<>();

    @Inject
    public RestoDetailViewModel(GetProductsUseCase getProductsUseCase, SavedStateHandle savedStateHandle) {
        this.getProductsUseCase = getProductsUseCase;
        this.restaurantId = Integer.parseInt(savedStateHandle.get("id"));
    }

    public MutableLiveData<String> getErrorProductsMsg() {
        return errorGetProductsMsg;
    }

    public LiveData<GetProductsDto.Response> getProducts() {
        if (products.getValue() == null) {
            fetchProducts();
        }
        return products;
    }

    private void fetchProducts() {
        getProductsUseCase.execute(restaurantId, new Callback<GetProductsDto.Response>() {
            @Override
            public void onResponse(Call<GetProductsDto.Response> call, Response<GetProductsDto.Response> response) {
                if(response.isSuccessful()) {
                    products.postValue(response.body());
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                        errorGetProductsMsg.postValue(errorResponse.message);
                    } catch (Throwable e) {
                        Log.e("UNKNOWN GET PRODUCTS ERROR", e.toString());
                        errorGetProductsMsg.postValue("Gagal menerima data produk");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductsDto.Response> call, Throwable t) {
                Log.e("UNKNOWN GET PRODUCTS ERROR", t.toString());
            }
        });
    }
}
