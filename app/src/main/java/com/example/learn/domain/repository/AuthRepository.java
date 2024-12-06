package com.example.learn.domain.repository;

import com.example.learn.data.dto.auth.GetProfileDto;
import com.example.learn.data.dto.auth.GoogleLoginDto;
import com.example.learn.data.dto.auth.LoginDto;
import com.example.learn.data.dto.auth.LogoutDto;

import retrofit2.Call;

public interface AuthRepository {

    Call<LoginDto.Response> login(LoginDto.Body body);

    Call<GoogleLoginDto.Response> googleLogin(GoogleLoginDto.Body body);

    Call<LogoutDto.Response> logout(LogoutDto.Body body);

    Call<GetProfileDto> getProfile();
}
