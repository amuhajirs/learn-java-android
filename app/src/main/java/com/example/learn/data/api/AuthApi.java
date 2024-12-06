package com.example.learn.data.api;

import com.example.learn.data.dto.auth.GetProfileDto;
import com.example.learn.data.dto.auth.GoogleLoginDto;
import com.example.learn.data.dto.auth.LoginDto;
import com.example.learn.data.dto.auth.LogoutDto;
import com.example.learn.data.dto.auth.RefreshDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/v1/user/auth/login")
    Call<LoginDto.Response> login(@Body LoginDto.Body body);

    @POST("/api/v1/user/auth/google")
    Call<GoogleLoginDto.Response> googleLogin(@Body GoogleLoginDto.Body body);

    @POST("/api/v1/user/auth/logout")
    Call<LogoutDto.Response> logout(@Body LogoutDto.Body body);

    @POST("/api/v1/user/auth/refresh")
    Call<RefreshDto.Response> refresh(@Body RefreshDto.Body body);

    @GET("/api/v1/user/auth/profile")
    Call<GetProfileDto> getProfile();
}
