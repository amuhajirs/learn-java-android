package com.example.learn.features.transactions.data.dto;

import com.example.learn.features.resto.data.dto.ProductDto;
import com.google.gson.annotations.SerializedName;

public class OrderItemDto {
    public int id;

    @SerializedName("order_id")
    public int orderId;

    @SerializedName("product_id")
    public int productId;

    public int quantity;

    public int price;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    public ProductDto product;
}
