package com.example.learn.features.auth.data.dto;

import com.google.gson.annotations.SerializedName;

public class LoginDto {
    public static class Body {
        @SerializedName("email")
        final private String email;

        @SerializedName("password")
        final private String password;

        public Body(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public static class Response {
        public UserDto data;
        public String message;
        public String status;
        public TokenDto token;
    }
}
