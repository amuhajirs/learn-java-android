package com.example.learn.features.auth.data.dto;

import com.google.gson.annotations.SerializedName;

public class TokenDto {
    public String type;

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("refresh_token")
    public String refreshToken;
}
