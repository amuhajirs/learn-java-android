package com.example.learn.features.auth.domain.repository;

import com.example.learn.features.auth.data.dto.GetProfileDto;
import com.example.learn.features.auth.data.dto.GoogleLoginDto;
import com.example.learn.features.auth.data.dto.LoginDto;
import com.example.learn.features.auth.data.dto.LogoutDto;

import retrofit2.Call;

public interface AuthRepository {

    Call<LoginDto.Response> login(LoginDto.Body body);

    Call<GoogleLoginDto.Response> googleLogin(GoogleLoginDto.Body body);

    Call<LogoutDto.Response> logout(LogoutDto.Body body);

    Call<GetProfileDto> getProfile();
}
