package com.example.learn.presentation.ui.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;

import com.example.learn.R;
import com.example.learn.presentation.ui.home.HomeActivity;
import com.example.learn.presentation.ui.login.LoginActivity;

import dagger.hilt.android.AndroidEntryPoint;

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    private SplashViewModel viewModel;
    private boolean isReadyToNavigate = false;
    private int MIN_SPLASH_DURATION = 0;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MIN_SPLASH_DURATION = getResources().getInteger(R.integer.splash_animation_duration);

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        startTime = System.currentTimeMillis();

        splashScreen.setKeepOnScreenCondition(() -> !isReadyToNavigate);

        checkLoginStatus();
    }

    private void checkLoginStatus() {
        Handler handler = new Handler();
        viewModel.checkAuth(new SplashViewModel.CheckAuthCb() {
            @Override
            public void onAuthenticated() {
                delayNavigation(HomeActivity.class, handler);
            }

            @Override
            public void onGuest() {
                delayNavigation(LoginActivity.class, handler);
            }
        });
    }

    private void delayNavigation(Class<?> targetActivity, Handler handler) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long remainingTime = MIN_SPLASH_DURATION - elapsedTime;

        if (remainingTime > 0) {
            handler.postDelayed(() -> navigateTo(targetActivity), remainingTime);
        } else {
            navigateTo(targetActivity);
        }
    }

    private void navigateTo(Class<?> targetActivity) {
        isReadyToNavigate = true;
        startActivity(new Intent(SplashActivity.this, targetActivity));
        finish();
    }
}
