package com.example.learn.features.auth.presentation.ui;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learn.features.auth.data.dto.GoogleLoginDto;
import com.example.learn.features.auth.data.dto.LoginDto;
import com.example.learn.features.auth.domain.usecase.GoogleLoginUseCase;
import com.example.learn.features.auth.domain.usecase.LoginUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final LoginUseCase loginUseCase;
    private final GoogleLoginUseCase googleLoginUseCase;
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase, GoogleLoginUseCase googleLoginUseCase) {
        this.loginUseCase = loginUseCase;
        this.googleLoginUseCase = googleLoginUseCase;
    }

    public LiveData<String> getLoginSuccess() {
        return successMessage;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login(String email, String password) {
        loginUseCase.execute(email, password, new LoginUseCase.ExecuteCb() {
            @Override
            public void onSuccess(LoginDto.Response response) {
                successMessage.postValue(response.message);
            }

            @Override
            public void onFailure(String msg) {
                errorMessage.postValue(msg);
            }
        });
    }

    public void googleLogin(Context context) {
        googleLoginUseCase.execute(context, new GoogleLoginUseCase.ExecuteCb() {
            @Override
            public void onSuccess(GoogleLoginDto.Response response) {
                successMessage.postValue(response.message);
            }

            @Override
            public void onFailure(String msg) {
                errorMessage.postValue(msg);
            }
        });

    }
}
