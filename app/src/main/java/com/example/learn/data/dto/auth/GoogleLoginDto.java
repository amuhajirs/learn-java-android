package com.example.learn.data.dto.auth;

import com.google.gson.annotations.SerializedName;

public class GoogleLoginDto {
    public static class Body {
        @SerializedName("id_token")
        public String idToken;

        public Body(String idToken) {
            this.idToken = idToken;
        }
    }

    public static class Response {
        public UserDto data;
        public String message;
        public String status;
        public TokenDto token;
        @SerializedName("is_setted")
        public boolean isSetted;
    }
}
