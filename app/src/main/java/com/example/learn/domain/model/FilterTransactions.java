package com.example.learn.domain.model;

public class FilterTransactions {
    public String search="";
    public String status="";
    public String categoryId="";
    public String date="";

    public FilterTransactions() {}

    public FilterTransactions(String search, String status, String categoryId, String date) {
        this.search = search;
        this.status = status;
        this.categoryId = categoryId;
        this.date = date;
    }

    public FilterTransactions copy() {
        return new FilterTransactions(this.search, this.status, this.categoryId, this.date);
    }
}
