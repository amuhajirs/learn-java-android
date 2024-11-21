package com.example.test.data.dto;

import com.google.gson.annotations.SerializedName;

public class LoginDto {
    public static class Body {
        @SerializedName("email")
        private String email;

        @SerializedName("password")
        private String password;

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
