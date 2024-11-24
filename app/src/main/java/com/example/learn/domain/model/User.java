package com.example.learn.domain.model;

import com.google.gson.annotations.SerializedName;

public class User {
    public int id;
    public String name;
    public  String email;
    public String phone;
    public String avatar;

    @SerializedName("wallet_id")
    public int walletId;

    @SerializedName("role_id")
    public int roleId;
    @SerializedName("updated_at")
    public String updatedAt;

    @SerializedName("created_at")
    public String createdAt;

    public Wallet wallet;
}
