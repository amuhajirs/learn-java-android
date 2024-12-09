package com.example.learn.features.auth.domain.usecase;

import com.example.learn.features.auth.data.dto.GetProfileDto;
import com.example.learn.features.auth.domain.repository.AuthRepository;
import com.example.learn.features.splash.presentation.ui.SplashViewModel;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetProfileUseCase {
    private final AuthRepository authRepository;

    @Inject
    public GetProfileUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(SplashViewModel.CheckAuthCb cb) {
        authRepository.getProfile().enqueue(new Callback<GetProfileDto>() {
            @Override
            public void onResponse(Call<GetProfileDto> call, Response<GetProfileDto> response) {
                if (response.isSuccessful()) {
                    cb.onAuthenticated(response.body());
                } else {
                    cb.onGuest();
                }
            }

            @Override
            public void onFailure(Call<GetProfileDto> call, Throwable t) {
                cb.onGuest();
            }
        });
    }
}
