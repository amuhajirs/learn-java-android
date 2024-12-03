package com.example.learn.data.dto.trx;

import com.example.learn.data.dto.resto.RestaurantDto;
import com.google.gson.annotations.SerializedName;

public class CartDto {
    public int id;

    @SerializedName("user_id")
    public int userId;

    @SerializedName("restaurant_id")
    public int restaurantId;

    public String notes;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    public RestaurantDto restaurant;
    public CartItemDto[] items;
}
