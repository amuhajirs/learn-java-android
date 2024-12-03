package com.example.learn.presentation.ui.resto_detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.learn.data.dto.ErrorDto;
import com.example.learn.data.dto.resto.GetProductsDto;
import com.example.learn.data.dto.resto.ProductDto;
import com.example.learn.data.dto.resto.ProductsPerCategory;
import com.example.learn.domain.usecase.GetProductsUseCase;
import com.example.learn.helper.utils.Resource;
import com.example.learn.presentation.adapter.ProductAdapter;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class RestoDetailViewModel extends ViewModel implements ProductAdapter.QuantitiesViewModel {
    private final GetProductsUseCase getProductsUseCase;
    private final int restaurantId;

    private final MutableLiveData<Resource<GetProductsDto.Response>> productsState = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, Integer>> productQuantities = new MutableLiveData<>(new HashMap<>());

    @Inject
    public RestoDetailViewModel(GetProductsUseCase getProductsUseCase, SavedStateHandle savedStateHandle) {
        this.getProductsUseCase = getProductsUseCase;
        this.restaurantId = Integer.parseInt(savedStateHandle.get("id"));
    }

    public LiveData<Resource<GetProductsDto.Response>> getProducts() {
        if (productsState.getValue() == null) {
            fetchProducts();
        }
        return productsState;
    }

    @Override
    public LiveData<Map<Integer, Integer>> getQuantities() {
        return productQuantities;
    }

    @Override
    public void updateQuantity(int productId, int quantity) {
        Map<Integer, Integer> currentQuantities = productQuantities.getValue();
        if (currentQuantities != null) {
            currentQuantities.put(productId, quantity);
            productQuantities.setValue(currentQuantities);
        }
    }

    public int getTotalQuantities() {
        int total = 0;
        Map<Integer, Integer> currentQuantities = productQuantities.getValue();
        if (currentQuantities != null) {
            for (Integer quantity : currentQuantities.values()) {
                total += quantity;
            }
        }
        return total;
    }

    public int getTotalAmount() {
        int total = 0;
        Map<Integer, Integer> currentQuantities = productQuantities.getValue();
        ProductsPerCategory[] productCategories = productsState.getValue().getData().data;

        if (currentQuantities != null) {
            for (ProductsPerCategory productCategory: productCategories) {
                for (ProductDto product: productCategory.data) {
                    Integer quantity = currentQuantities.get(product.id);
                    if (quantity != null) {
                        total += product.price * quantity;
                    }
                }
            }
        }

        return total;
    }

    public void fetchProducts() {
        productsState.setValue(Resource.loading());

        getProductsUseCase.execute(restaurantId, new Callback<GetProductsDto.Response>() {
            @Override
            public void onResponse(Call<GetProductsDto.Response> call, Response<GetProductsDto.Response> response) {
                if(response.isSuccessful()) {
                    productsState.postValue(Resource.success(response.body()));
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                        productsState.postValue(Resource.error(errorResponse.message));
                    } catch (Throwable e) {
                        Log.e("UNKNOWN GET PRODUCTS ERROR", e.toString());
                        productsState.postValue(Resource.error(e.toString()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProductsDto.Response> call, Throwable t) {
                productsState.postValue(Resource.error(t.toString()));
                Log.e("UNKNOWN GET PRODUCTS ERROR", t.toString());
            }
        });
    }
}
