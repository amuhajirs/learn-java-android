package com.example.learn.domain.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    public int id;
    @SerializedName("restaurant_id")
    public int restaurantId;
    public String name;
    public String description;
    public String image;
    @SerializedName("category_id")
    public int categoryId;
    public int price;
    public int stock;
    public int sold;
    @SerializedName("is_active")
    public boolean isActive;
    public int like;
    public int dislike;
    @SerializedName("is_liked")
    public int isLiked;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;
}
