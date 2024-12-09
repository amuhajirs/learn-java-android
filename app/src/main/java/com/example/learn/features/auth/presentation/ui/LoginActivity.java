package com.example.learn.features.auth.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learn.R;
import com.example.learn.databinding.ActivityLoginBinding;
import com.example.learn.features.main.presentation.ui.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        listeners();
        stateObserver();
    }

    private void listeners() {
        binding.loginBtn.setOnClickListener(this::handleLogin);
        binding.googleBtn.setOnClickListener(v -> viewModel.googleLogin(this));
    }

    private void stateObserver() {
        viewModel.getLoginSuccess().observe(this, message -> {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        viewModel.getErrorMessage().observe(this, message -> {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            binding.loginBtn.setEnabled(true);
        });
    }

    public void handleLogin(View v) {
        v.setEnabled(false);

        String email = binding.loginEmail.getText().toString();
        String pw = binding.loginPassword.getText().toString();

        viewModel.login(email, pw);
    }
}
