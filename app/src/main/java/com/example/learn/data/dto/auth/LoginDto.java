package com.example.learn.data.dto.auth;

import com.example.learn.domain.model.Token;
import com.example.learn.domain.model.User;
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
        public User data;
        public String message;
        public String status;
        public Token token;
    }
}
