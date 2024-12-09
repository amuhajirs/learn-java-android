package com.example.learn.features.transactions.data.dto;

import com.google.gson.annotations.SerializedName;

public class TransactionDto {
    public int id;
    @SerializedName("transaction_id")
    public String transactionCode;
    @SerializedName("user_id")
    public int userId;
    public String type;
    public TransactionStatus status;
    public int amount;
    @SerializedName("payment_method")
    public String paymentMethod;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;

    enum TransactionStatus {
        SUCCESS,
        CANCELED
    }
}
