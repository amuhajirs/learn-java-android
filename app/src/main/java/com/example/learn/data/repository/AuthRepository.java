package com.example.learn.data.repository;

import com.example.learn.data.api.AuthApi;
import com.example.learn.data.api.RetrofitClient;
import com.example.learn.data.dto.auth.GetProfileDto;
import com.example.learn.data.dto.auth.LoginDto;
import com.example.learn.data.dto.auth.LogoutDto;

import retrofit2.Call;

public class AuthRepository {
    private final AuthApi authApi;

    public AuthRepository() {
        authApi = RetrofitClient.getClient().create(AuthApi.class);
    }

    public Call<LoginDto.Response> login(LoginDto.Body body) { return authApi.login(body); }

    public Call<LogoutDto.Response> logout(LogoutDto.Body body) { return authApi.logout(body); }

    public Call<GetProfileDto> getProfile() { return authApi.getProfile(); }
}
