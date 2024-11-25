package com.example.learn.presentation.ui.resto_detail;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learn.presentation.ui.login.LoginViewModel;

public class RestoDetailActivity extends AppCompatActivity {
    RestoDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RestoDetailViewModel.class);
    }
}