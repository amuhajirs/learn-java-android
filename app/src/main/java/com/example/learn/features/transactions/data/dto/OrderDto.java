package com.example.learn.features.transactions.data.dto;

import com.example.learn.features.resto.data.dto.RestaurantDto;
import com.google.gson.annotations.SerializedName;

public class OrderDto {
    public enum OrderStatus {
        WAITING,
        INPROGRESS,
        READY,
        SUCCESS,
        CANCELED,
    }

    public int id;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("restaurant_id")
    public int restaurantId;

    public String notes;

    public int amount;

    public OrderStatus status;

    @SerializedName("is_preorder")
    public boolean isPreorder;

    @SerializedName("transaction_id")
    public int transactionId;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    public RestaurantDto restaurant;

    public OrderItemDto[] items;

    public TransactionDto transaction;
}
