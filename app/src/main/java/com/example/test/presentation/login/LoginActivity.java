package com.example.test.presentation.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.constant.Constant;
import com.example.test.data.dto.LoginDto;
import com.example.test.data.repository.AuthRepository;
import com.example.test.presentation.home.HomeActivity;
import com.example.test.utils.DataStoreSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText emailEl, passwordEl;
    Button btnLoginEl, btnGoogleEl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DataStoreSingleton.getInstance().getValue(Constant.ACC_TOKEN, s -> {
            Log.d("ACC_TOKEN", s);
            if(!s.equals("")) {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        emailEl = findViewById(R.id.login_email);
        passwordEl = findViewById(R.id.login_password);
        btnLoginEl = (Button) findViewById(R.id.login_btn);
        btnGoogleEl = (Button) findViewById(R.id.google_btn);

        btnLoginEl.setOnClickListener(v -> {
            handleLogin(v);
        });
        btnGoogleEl.setOnClickListener(v -> {
            handleGoogle(v);
        });
    }

    public void handleLogin(View v) {
        v.setEnabled(false);

        String email = this.emailEl.getText().toString();
        String pw = this.passwordEl.getText().toString();

        AuthRepository repo = new AuthRepository();

        repo.login(new LoginDto.Body(email, pw)).enqueue(new Callback<LoginDto.Response>() {
            @Override
            public void onResponse(Call<LoginDto.Response> call, Response<LoginDto.Response> response) {
                Log.d("LOGIN DEBUG RESPONSE", response.toString());
                Log.d("LOGIN DEBUG RAW", response.raw().toString());
                try {
                    Log.d("LOGIN DEBUG ERROR BODY", response.errorBody().string());
                } catch (Throwable e) {
                    Log.d("LOGIN DEBUG ERROR BODY", e.toString());
                }
                Log.d("LOGIN DEBUG HEADERS", response.headers().toString());

                if(response.isSuccessful()) {
                    LoginDto.Response loginResponse = response.body();
                    Log.d("LOGIN DEBUG SUCCESS", loginResponse.message);

                    DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();

                    Log.d("LOGIN DEBUG ACC_TOKEN", loginResponse.token.accessToken);
                    dataStoreSingleton.saveValue(Constant.ACC_TOKEN, loginResponse.token.accessToken);
                    Log.d("LOGIN DEBUG REF_TOKEN", loginResponse.token.refreshToken);
                    dataStoreSingleton.saveValue(Constant.REF_TOKEN, loginResponse.token.refreshToken);

                    Toast.makeText(LoginActivity.this, loginResponse.message, Toast.LENGTH_LONG).show();

                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<LoginDto.Response> call, Throwable t) {
                Log.d("LOGIN DEBUG ERROR", t.toString());
                Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_LONG).show();
                v.setEnabled(true);
            }
        });
    }

    public void handleGoogle(View v) {
        Log.d("GOOGLE CLICKED", "clicked");
    }
}
