package com.example.learn.presentation.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.learn.R;

public class CustomActionBar extends ConstraintLayout {

    private ImageButton backButton;

    public CustomActionBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomActionBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_action_bar, this, true);
        backButton = findViewById(R.id.back_btn);
    }

    public ImageButton getBackButton() {
        return backButton;
    }
}

