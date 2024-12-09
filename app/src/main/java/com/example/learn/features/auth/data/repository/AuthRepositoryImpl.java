package com.example.learn.features.auth.data.repository;

import com.example.learn.features.auth.data.api.AuthApi;
import com.example.learn.features.auth.data.dto.GetProfileDto;
import com.example.learn.features.auth.data.dto.GoogleLoginDto;
import com.example.learn.features.auth.data.dto.LoginDto;
import com.example.learn.features.auth.data.dto.LogoutDto;
import com.example.learn.features.auth.domain.repository.AuthRepository;

import retrofit2.Call;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthApi authApi;

    public AuthRepositoryImpl(AuthApi authApi) {
        this.authApi = authApi;
    }

    public Call<LoginDto.Response> login(LoginDto.Body body) {
        return authApi.login(body);
    }

    public Call<GoogleLoginDto.Response> googleLogin(GoogleLoginDto.Body body) {
        return authApi.googleLogin(body);
    }

    public Call<LogoutDto.Response> logout(LogoutDto.Body body) {
        return authApi.logout(body);
    }

    public Call<GetProfileDto> getProfile() {
        return authApi.getProfile();
    }
}
