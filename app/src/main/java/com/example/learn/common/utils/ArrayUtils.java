package com.example.learn.common.utils;

public class ArrayUtils {
    public static <T> T find(T[] list, FindCb<T> cb) {
        for (T item : list) {
            if (cb.eachElement(item)) {
                return item;
            }
        }
        return null;
    }

    public interface FindCb<T> {
        boolean eachElement(T item);
    }
}
