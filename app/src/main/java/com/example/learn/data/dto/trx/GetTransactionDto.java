package com.example.learn.data.dto.trx;

import com.example.learn.data.dto.MetaPagination;

public class GetTransactionDto {
    static public class Response {
        public MetaPagination meta;
        public OrderDto[] data;
    }
}
