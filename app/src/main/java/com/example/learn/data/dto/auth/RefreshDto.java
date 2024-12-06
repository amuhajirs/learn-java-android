package com.example.learn.data.dto.auth;

import com.google.gson.annotations.SerializedName;

public class RefreshDto {
    public static class Body {
        @SerializedName("refresh_token")
        public String refreshToken;

        public Body(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class Response {
        public String status;
        public TokenDto token;
    }
}