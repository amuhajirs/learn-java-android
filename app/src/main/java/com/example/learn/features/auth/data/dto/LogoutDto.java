package com.example.learn.features.auth.data.dto;

import com.google.gson.annotations.SerializedName;

public class LogoutDto {
    public static class Body {
        @SerializedName("refresh_token")
        public String refreshToken;

        public Body(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class Response {
        public String message;
        public String status;
    }
}
