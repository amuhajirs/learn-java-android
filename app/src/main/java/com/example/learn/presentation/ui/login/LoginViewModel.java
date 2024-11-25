package com.example.learn.presentation.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learn.data.dto.ErrorDto;
import com.example.learn.data.dto.auth.LoginDto;
import com.example.learn.domain.usecase.LoginUseCase;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final LoginUseCase loginUseCase;
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public LiveData<String> getLoginSuccess() {
        return successMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        loginUseCase.execute(email, password, new Callback<LoginDto.Response>() {
            @Override
            public void onResponse(Call<LoginDto.Response> call, Response<LoginDto.Response> response) {
                if (response.isSuccessful()) {
                    successMessage.postValue(response.body().message);
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                        errorMessage.postValue(errorResponse.message);
                    } catch (Exception e) {
                        Log.e("UNKNOWN LOGIN ERROR", e.toString());
                        errorMessage.postValue("Login gagal");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginDto.Response> call, Throwable t) {
                Log.e("UNKNOWN LOGIN ERROR", t.toString());
                errorMessage.postValue("Login gagal");
            }
        });
    }
}
