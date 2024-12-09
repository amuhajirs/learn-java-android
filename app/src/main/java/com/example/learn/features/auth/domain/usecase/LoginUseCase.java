package com.example.learn.features.auth.domain.usecase;

import com.example.learn.shared.data.dto.ErrorDto;
import com.example.learn.features.auth.data.dto.LoginDto;
import com.example.learn.features.auth.domain.repository.AuthRepository;
import com.example.learn.common.constant.DatastoreConst;
import com.example.learn.common.utils.DataStoreSingleton;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUseCase {
    private final AuthRepository authRepository;

    @Inject
    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, ExecuteCb cb) {
        authRepository.login(new LoginDto.Body(email, password)).enqueue(new Callback<LoginDto.Response>() {
            @Override
            public void onResponse(Call<LoginDto.Response> call, Response<LoginDto.Response> response) {
                if (response.isSuccessful()) {
                    LoginDto.Response loginResponse = response.body();

                    DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
                    dataStoreSingleton.saveValue(DatastoreConst.ACC_TOKEN, loginResponse.token.accessToken);
                    dataStoreSingleton.saveValue(DatastoreConst.REF_TOKEN, loginResponse.token.refreshToken);
                    dataStoreSingleton.saveValue(DatastoreConst.USER_NAME, loginResponse.data.name);
                    dataStoreSingleton.saveValue(DatastoreConst.USER_EMAIL, loginResponse.data.email);
                    dataStoreSingleton.saveValue(DatastoreConst.USER_PHONE, loginResponse.data.phone);
                    dataStoreSingleton.saveValue(DatastoreConst.USER_AVATAR, loginResponse.data.avatar);

                    cb.onSuccess(loginResponse);
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();
                        ErrorDto errorResponse = new Gson().fromJson(errorBody, ErrorDto.class);
                        cb.onFailure(errorResponse.message);
                    } catch (IOException e) {
                        cb.onFailure(e.toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginDto.Response> call, Throwable t) {
                cb.onFailure(t.toString());
            }
        });
    }

    public interface ExecuteCb {
        void onSuccess(LoginDto.Response response);
        void onFailure(String msg);
    }
}
