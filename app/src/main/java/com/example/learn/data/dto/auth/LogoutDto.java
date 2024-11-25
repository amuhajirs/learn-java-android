package com.example.learn.data.dto.auth;

import com.google.gson.annotations.SerializedName;

public class LogoutDto {
    public static class Body {
        @SerializedName("refresh_token")
        final private String refreshToken;

        public Body(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class Response {
        public String message;
        public String status;
    }
}
