package com.example.learn.features.auth.domain.usecase;

import com.example.learn.common.constant.DatastoreConst;
import com.example.learn.common.utils.DataStoreSingleton;
import com.example.learn.features.auth.data.dto.LogoutDto;
import com.example.learn.features.auth.domain.repository.AuthRepository;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoutUseCase {
    private final AuthRepository authRepository;

    @Inject
    public LogoutUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String refreshToken, Callback<LogoutDto.Response> callback) {
        authRepository.logout(new LogoutDto.Body(refreshToken)).enqueue(new Callback<LogoutDto.Response>() {
            @Override
            public void onResponse(Call<LogoutDto.Response> call, Response<LogoutDto.Response> response) {
                if (response.isSuccessful()) {
                    DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();
                    dataStoreSingleton.saveValue(DatastoreConst.ACC_TOKEN, "");
                    dataStoreSingleton.saveValue(DatastoreConst.REF_TOKEN, "");
                    dataStoreSingleton.saveValue(DatastoreConst.USER_NAME, "");
                    dataStoreSingleton.saveValue(DatastoreConst.USER_EMAIL, "");
                    dataStoreSingleton.saveValue(DatastoreConst.USER_PHONE, "");
                    dataStoreSingleton.saveValue(DatastoreConst.USER_AVATAR, "");
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<LogoutDto.Response> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
