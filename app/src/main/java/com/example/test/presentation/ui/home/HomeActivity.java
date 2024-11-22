package com.example.test.presentation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.domain.model.Restaurant;
import com.example.test.presentation.adapter.RestoAdapter;
import com.example.test.presentation.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity {
    private ImageButton btnLogoutEl;
    private HomeViewModel viewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        btnLogoutEl = findViewById(R.id.logout_btn);
        recyclerView = findViewById(R.id.resto_recycler);

        btnLogoutEl.setOnClickListener(this::handleLogout);

        handleGetRestos();
    }

    public void handleGetRestos() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RestoAdapter restoAdapter = new RestoAdapter();
        recyclerView.setAdapter(restoAdapter);

        viewModel.getRestos().observe(this, restos -> {
            if(restos != null) {
                ArrayList<Restaurant> restoList = new ArrayList<>();
                Collections.addAll(restoList, restos.data[0].data);

                restoAdapter.setRestos(restoList, restos.data[0].category);
            }
        });
    }

    public void handleLogout(View v) {
        v.setEnabled(false);

        viewModel.logout();

        viewModel.getLogoutSuccessMsg().observe(this, msg -> {
            Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        viewModel.getLogoutErrorMsg().observe(this, msg -> {
            Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG).show();
        });

        v.setEnabled(true);
    }
}