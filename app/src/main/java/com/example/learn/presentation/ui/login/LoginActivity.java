package com.example.learn.presentation.ui.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.GetPasswordOption;
import androidx.credentials.GetPublicKeyCredentialOption;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.lifecycle.ViewModelProvider;

import com.example.learn.R;
import com.example.learn.common.constant.ApiConst;
import com.example.learn.presentation.ui.main.MainActivity;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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

        listeners();
        stateObserver();
    }

    private void listeners() {
        btnLoginEl.setOnClickListener(this::handleLogin);
        btnGoogleEl.setOnClickListener(v -> viewModel.googleLogin(this));
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
            btnLoginEl.setEnabled(true);
        });
    }

    public void handleLogin(View v) {
        v.setEnabled(false);

        String email = emailEl.getText().toString();
        String pw = passwordEl.getText().toString();

        viewModel.login(email, pw);
    }
}
