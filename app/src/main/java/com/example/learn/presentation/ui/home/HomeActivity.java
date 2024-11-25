package com.example.learn.presentation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.data.dto.resto.RestaurantsPerCategory;
import com.example.learn.presentation.adapter.CategoryRestoAdapter;
import com.example.learn.presentation.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {
    private ImageButton btnLogoutEl;
    private HomeViewModel viewModel;
    private RecyclerView categoryRestoRecycler;
    private CategoryRestoAdapter categoryRestoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        btnLogoutEl = findViewById(R.id.logout_btn);
        categoryRestoRecycler = findViewById(R.id.category_resto_recycler);

        btnLogoutEl.setOnClickListener(this::handleLogout);

        viewModel.getLogoutSuccessMsg().observe(this, msg -> {
            Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        viewModel.getLogoutErrorMsg().observe(this, msg -> {
            Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_LONG).show();
            btnLogoutEl.setEnabled(true);
        });

        viewModel.getRestos().observe(this, restos -> {
            if (restos != null) {
                ArrayList<RestaurantsPerCategory> restoCategoryList = new ArrayList<>();
                Collections.addAll(restoCategoryList, restos.data);
                handleGetRestos(restoCategoryList);
            }
        });
    }

    public void handleGetRestos(List<RestaurantsPerCategory> restoCategories) {
        categoryRestoAdapter = new CategoryRestoAdapter(this, restoCategories);

        categoryRestoRecycler.setAdapter(categoryRestoAdapter);
        categoryRestoRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoryRestoRecycler.setNestedScrollingEnabled(false);

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        categoryRestoRecycler.setRecycledViewPool(viewPool);
    }

    public void handleLogout(View v) {
        v.setEnabled(false);

        viewModel.logout();
    }
}