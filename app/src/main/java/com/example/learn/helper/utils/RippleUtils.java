package com.example.learn.helper.utils;

import android.content.Context;
import android.util.TypedValue;

public class RippleUtils {
    public static int getDefaultRippleColor(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorControlHighlight, typedValue, true);
        return typedValue.data;
    }
}
