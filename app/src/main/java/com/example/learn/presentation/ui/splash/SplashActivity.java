package com.example.learn.presentation.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learn.R;
import com.example.learn.presentation.ui.home.HomeActivity;
import com.example.learn.presentation.ui.login.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        Handler handler = new Handler();

        viewModel.checkAuth(new SplashViewModel.CheckAuthCb() {
            @Override
            public void onAuthenticated() {
                handler.postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }, 1000);
            }

            @Override
            public void onGuest() {
                handler.postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }, 1000);
            }
        });
    }
}
