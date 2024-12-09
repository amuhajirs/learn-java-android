package com.example.learn.features.transactions.data.dto;

import com.google.gson.annotations.SerializedName;

public class CreateCartDto {
    static public class Body {
        @SerializedName("product_id")
        public int productId;

        public int quantity;

        public Body(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }

    static public class Response {
        public CartDto data;
        public String message;
        public String status;
    }
}
