package com.example.learn.features.transactions.data.dto;

import com.example.learn.shared.data.dto.MetaPagination;

public class GetTransactionDto {
    static public class Response {
        public MetaPagination meta;
        public OrderDto[] data;
    }
}
