package com.example.test.domain.model;

import com.google.gson.annotations.SerializedName;

public class Restaurant {
    public int id;
    public String name;
    public String avatar;
    public String banner;

    @SerializedName("category_id")
    public int categoryId;

    @SerializedName("owner_id")
    public int ownerId;
    @SerializedName("is_open")
    public boolean isOpen;

    @SerializedName("rating_avg")
    public float ratingAvg;

    @SerializedName("rating_count")
    public int ratingCount;

    @SerializedName("updated_at")
    public String updatedAt;

    @SerializedName("created_at")
    public String createdAt;
}
