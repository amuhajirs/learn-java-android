package com.example.learn.domain.usecase;

import com.example.learn.data.dto.auth.LoginDto;
import com.example.learn.domain.repository.AuthRepository;
import com.example.learn.helper.constant.DatastoreConst;
import com.example.learn.helper.utils.DataStoreSingleton;

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

    public void execute(String email, String password, Callback<LoginDto.Response> callback) {
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

                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<LoginDto.Response> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
