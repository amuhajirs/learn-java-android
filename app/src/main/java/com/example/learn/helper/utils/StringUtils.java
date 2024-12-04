package com.example.learn.helper.utils;

import java.text.DecimalFormat;

public class StringUtils {
    public static String formatCurrency(int amount) {
        return "Rp " + (new DecimalFormat("#,###")).format(amount).replace(",", ".");
    }
}
