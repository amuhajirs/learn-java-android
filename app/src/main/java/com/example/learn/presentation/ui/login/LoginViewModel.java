package com.example.learn.presentation.ui.login;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learn.common.constant.ApiConst;
import com.example.learn.data.dto.ErrorDto;
import com.example.learn.data.dto.auth.GoogleLoginDto;
import com.example.learn.data.dto.auth.LoginDto;
import com.example.learn.domain.usecase.GoogleLoginUseCase;
import com.example.learn.domain.usecase.LoginUseCase;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.gson.Gson;

import java.util.UUID;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
