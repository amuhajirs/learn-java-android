package com.example.learn.domain.repository;

import com.example.learn.data.dto.auth.GetProfileDto;
import com.example.learn.data.dto.auth.LoginDto;
import com.example.learn.data.dto.auth.LogoutDto;

import retrofit2.Call;

public interface AuthRepository {

    public Call<LoginDto.Response> login(LoginDto.Body body);

    public Call<LogoutDto.Response> logout(LogoutDto.Body body);

    public Call<GetProfileDto> getProfile();
}
