package com.example.learn.features.transactions.data.dto;

import com.example.learn.features.resto.data.dto.ProductDto;
import com.google.gson.annotations.SerializedName;

public class CartItemDto {
    public int id;

    @SerializedName("cart_id")
    public int cartId;

    @SerializedName("product_id")
    public int productId;

    public int quantity;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    public ProductDto product;
}
