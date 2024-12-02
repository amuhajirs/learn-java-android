package com.example.learn.data.dto.auth;

import com.google.gson.annotations.SerializedName;

public class WalletDto {
    public int id;
    public int balance;
    @SerializedName("updated_at")
    public String updatedAt;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("is_pin_set")
    public boolean isPinSet;
}
