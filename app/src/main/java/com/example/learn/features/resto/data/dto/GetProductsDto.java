package com.example.learn.features.resto.data.dto;

public class GetProductsDto {
    static public class Query {
        public String search= "";
        public String page="1";
        public String limit="10";
        public String order="name";
        public String direction="asc";
        public String categoryId="";

        public Query() {
        }

        public Query(String search, String page, String limit, String order, String direction, String categoryId) {
            this.search = search;
            this.page = page;
            this.limit = limit;
            this.order = order;
            this.direction = direction;
            this.categoryId = categoryId;
        }

        public Query copy() {
            return new Query(this.search, this.page, this.limit, this.order, this.direction, this.categoryId);
        }
    }

    static public class Response {
        public MetaGetRestos meta;
        public ProductsPerCategory[] data;
    }
}
