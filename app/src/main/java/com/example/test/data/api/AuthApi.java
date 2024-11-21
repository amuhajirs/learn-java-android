package com.example.test.data.api;

import com.example.test.data.dto.GetProfileDto;
import com.example.test.data.dto.LoginDto;
import com.example.test.data.dto.LogoutDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/v1/user/auth/login")
    Call<LoginDto.Response> login(@Body LoginDto.Body body);

    @POST("/api/v1/user/auth/logout")
    Call<LogoutDto.Response> logout(@Body LogoutDto.Body body);

    @GET("/api/v1/user/auth/profile")
    Call<GetProfileDto> getProfile();
}
