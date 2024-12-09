package com.example.learn.features.transactions.data.dto;

import com.google.gson.annotations.SerializedName;

public class CreateOrderDto {
    static public class Body {
        @SerializedName("cart_id")
        public int cartId;

        @SerializedName("payment_method")
        final public String paymentMethod = "CASH";

        @SerializedName("is_preorder")
        final public boolean isPreorder = false;

        public Body(int cartId) {
            this.cartId = cartId;
        }
    }

    static public class Response {
        public OrderDto data;
        public String message;
        public String status;
    }
}
