package com.example.test.presentation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.constant.Constant;
import com.example.test.data.dto.LoginDto;
import com.example.test.data.dto.LogoutDto;
import com.example.test.data.repository.AuthRepository;
import com.example.test.presentation.login.LoginActivity;
import com.example.test.utils.DataStoreSingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    ImageButton btnLogoutEl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogoutEl = (ImageButton) findViewById(R.id.logout_btn);

        btnLogoutEl.setOnClickListener(v -> {
            handleLogout(v);
        });
    }

    public void handleLogout(View v) {
        v.setEnabled(false);

        DataStoreSingleton.getInstance().getValue(Constant.REF_TOKEN, s -> {
            AuthRepository repo = new AuthRepository();
            repo.logout(new LogoutDto.Body(s)).enqueue(new Callback<LogoutDto.Response>() {
                @Override
                public void onResponse(Call<LogoutDto.Response> call, Response<LogoutDto.Response> response) {
                    if(response.isSuccessful()) {
                        LogoutDto.Response logoutResponse = response.body();
                        DataStoreSingleton dataStoreSingleton = DataStoreSingleton.getInstance();

                        dataStoreSingleton.saveValue(Constant.ACC_TOKEN, "");
                        dataStoreSingleton.saveValue(Constant.REF_TOKEN, "");

                        Toast.makeText(HomeActivity.this, logoutResponse.message, Toast.LENGTH_LONG).show();

                        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(HomeActivity.this, "Logout gagal", Toast.LENGTH_LONG).show();
                        v.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<LogoutDto.Response> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Logout gagal", Toast.LENGTH_LONG).show();
                    v.setEnabled(true);
                }
            });
        });
    }
}