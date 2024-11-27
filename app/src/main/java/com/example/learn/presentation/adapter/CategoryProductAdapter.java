package com.example.learn.presentation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.data.dto.resto.ProductsPerCategory;
import com.example.learn.domain.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder> {
    private final List<ProductsPerCategory> productCategories;
    private final Context context;

    public CategoryProductAdapter(Context context, List<ProductsPerCategory> productCategories) {
        this.context = context;
        this.productCategories = productCategories;
    }

    @Override
    public CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_product, parent, false);
        return new CategoryProductViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryProductViewHolder holder, int position) {
        Log.d("CATEGORY PRODUCT", String.valueOf(position));
        if (productCategories == null) {
            return;
        }

        ProductsPerCategory productCategory = productCategories.get(position);
        holder.categoryName.setText(productCategory.category.name);

        ArrayList<Product> productList = new ArrayList<>();
        Collections.addAll(productList, productCategory.data);

        ProductAdapter productAdapter = new ProductAdapter();
        productAdapter.setProducts(productList, productCategory.category);

        holder.productRecycler.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
        Log.d("PRODUCT CATEGORY SIZE", String.valueOf(productCategories.size()));
        if (productCategories == null) {
            return 0;
        }
        return productCategories.size();
    }

    public static class CategoryProductViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView productRecycler;

        public CategoryProductViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            productRecycler = itemView.findViewById(R.id.product_recycler);

            Log.d("PRODUCT CATEGORY", productRecycler.toString());

            productRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            productRecycler.setNestedScrollingEnabled(false);
        }
    }
}
