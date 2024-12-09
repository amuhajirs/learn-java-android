package com.example.learn.features.my_resto.presentation.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learn.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyRestoActivity extends AppCompatActivity {
    MyRestoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resto);

        viewModel = new ViewModelProvider(this).get(MyRestoViewModel.class);
    }
}