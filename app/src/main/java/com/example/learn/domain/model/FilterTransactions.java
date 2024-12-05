package com.example.learn.domain.model;

public class FilterTransactions {
    public String status;
    public String categoryId;
    public String date;

    public FilterTransactions(String status, String categoryId, String date) {
        this.status = status;
        this.categoryId = categoryId;
        this.date = date;
    }
}
