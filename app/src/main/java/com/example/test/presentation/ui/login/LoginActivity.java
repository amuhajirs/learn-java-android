package com.example.test.presentation.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.test.R;
import com.example.test.presentation.ui.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEl, passwordEl;
    private Button btnLoginEl, btnGoogleEl;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        emailEl = findViewById(R.id.login_email);
        passwordEl = findViewById(R.id.login_password);
        btnLoginEl = findViewById(R.id.login_btn);
        btnGoogleEl = findViewById(R.id.google_btn);

        btnLoginEl.setOnClickListener(this::handleLogin);
        btnGoogleEl.setOnClickListener(this::handleGoogle);

        viewModel.getLoginSuccess().observe(this, message -> {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        viewModel.getErrorMessage().observe(this, message -> {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        });
    }

    public void handleLogin(View v) {
        v.setEnabled(false);

        String email = emailEl.getText().toString();
        String pw = passwordEl.getText().toString();

        viewModel.login(email, pw);

        v.setEnabled(true);
    }

    public void handleGoogle(View v) {
        Log.d("GOOGLE CLICKED", "clicked");
    }
}
