package com.example.learn.data.dto.resto;

import com.google.gson.annotations.SerializedName;

public class ProductDto {
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
    public Boolean isLiked;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;

//    public Product toProduct() {
//        return new Product(id, name, description, image, price, stock, sold, like, dislike, isLiked, 0);
//    }
}
