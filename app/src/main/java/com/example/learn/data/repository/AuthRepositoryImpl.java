package com.example.learn.data.repository;

import com.example.learn.data.api.AuthApi;
import com.example.learn.data.dto.auth.GetProfileDto;
import com.example.learn.data.dto.auth.GoogleLoginDto;
import com.example.learn.data.dto.auth.LoginDto;
import com.example.learn.data.dto.auth.LogoutDto;
import com.example.learn.domain.repository.AuthRepository;

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
