package com.example.learn.domain.model;

public class Filter {
    public String label;
    public String value;

    public Filter(String label, String value) {
        this.label = label;
        this.value = value;
    };

    public static Filter find(Filter[] filters, String value) {
        for (Filter filter : filters) {
            if (filter.value.equals(value)) {
                return filter; // Return the found product
            }
        }
        return null; // Return null if not found
    }
}
