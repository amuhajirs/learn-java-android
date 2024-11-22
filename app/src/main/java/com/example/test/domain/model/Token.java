package com.example.test.domain.model;

import com.google.gson.annotations.SerializedName;

public class Token {
    public String type;

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("refresh_token")
    public String refreshToken;
}
