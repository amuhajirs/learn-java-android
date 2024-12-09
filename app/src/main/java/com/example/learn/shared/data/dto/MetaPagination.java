package com.example.learn.shared.data.dto;

import com.google.gson.annotations.SerializedName;

public class MetaPagination {
    @SerializedName("current_page")
    public int currentPage;

    @SerializedName("per_page")
    public int perPage;

    @SerializedName("total")
    public int total;

    @SerializedName("last_page")
    public int lastPage;
}
