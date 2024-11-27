package com.example.learn.presentation.ui.resto_detail;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.data.dto.resto.ProductsPerCategory;
import com.example.learn.presentation.adapter.CategoryProductAdapter;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestoDetailActivity extends AppCompatActivity {
    private RestoDetailViewModel viewModel;
    private ImageButton backBtn;
    private ImageView restoAvatar, restoBanner;
    private TextView restoName, restoCategory, restoRating, restoRatingCount;
    private RecyclerView categoryProductRecycler;
    private CategoryProductAdapter categoryProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resto_detail);

        viewModel = new ViewModelProvider(this).get(RestoDetailViewModel.class);

        backBtn = findViewById(R.id.back_btn);
        restoBanner = findViewById(R.id.resto_banner);
        restoAvatar = findViewById(R.id.resto_avatar);
        restoName = findViewById(R.id.resto_name);
        restoCategory = findViewById(R.id.resto_category);
        restoRating = findViewById(R.id.resto_rating);
        restoRatingCount = findViewById(R.id.resto_rating_count);
        categoryProductRecycler = findViewById(R.id.category_product_recycler);

        categoryProductRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoryProductRecycler.setNestedScrollingEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Glide.with(restoBanner.getContext()).load(extras.getString("banner")).into(restoBanner);
            Glide.with(restoAvatar.getContext()).load(extras.getString("avatar")).into(restoAvatar);
            restoName.setText(extras.getString("name"));
            restoCategory.setText(extras.getString("category"));
            restoRating.setText(String.format("%.1f", extras.getFloat("rating_avg")));
            restoRatingCount.setText(String.format("(%d)", extras.getInt("rating_count")));

            Log.d("RESTO ID", extras.getString("id"));
        } else {
            finish();
        }

        listeners();
        stateObserver();
    }

    private void listeners() {
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void stateObserver() {
        viewModel.getProducts().observe(this, products -> {
            if (products != null) {
                ArrayList<ProductsPerCategory> productCategoryList = new ArrayList<>();
                Collections.addAll(productCategoryList, products.data);
                categoryProductAdapter = new CategoryProductAdapter(this, productCategoryList);
                categoryProductRecycler.setAdapter(categoryProductAdapter);
                Log.d("PRODUCT CATEGORY", "attached");
            }
        });

        viewModel.getErrorProductsMsg().observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        });
    }
}