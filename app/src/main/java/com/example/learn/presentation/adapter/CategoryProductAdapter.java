package com.example.learn.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.data.dto.resto.ProductDto;
import com.example.learn.data.dto.resto.ProductsPerCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder> {
    private final Context context;
    private final ProductAdapter.QuantitiesViewModel viewModel;
    private final ProductAdapter.OnClickProduct onClickProduct;
    private List<ProductsPerCategory> productCategories;

    public CategoryProductAdapter(Context context, ProductAdapter.QuantitiesViewModel viewModel, ProductAdapter.OnClickProduct onClickProduct) {
        this.context = context;
        this.viewModel = viewModel;
        this.onClickProduct = onClickProduct;
    }

    public void setProductCategories(List<ProductsPerCategory> productCategories) {
        this.productCategories = productCategories;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_product, parent, false);
        return new CategoryProductViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryProductViewHolder holder, int position) {
        if (productCategories == null) {
            return;
        }

        ProductsPerCategory productCategory = productCategories.get(position);
        holder.categoryName.setText(productCategory.category.name);

        ArrayList<ProductDto> productList = new ArrayList<>(Arrays.asList(productCategory.data));

        ProductAdapter productAdapter = new ProductAdapter(context, viewModel, onClickProduct);
        productAdapter.setProducts(productList);

        holder.productRecycler.setAdapter(productAdapter);
    }

    @Override
    public int getItemCount() {
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

            productRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            productRecycler.setNestedScrollingEnabled(false);
        }
    }
}
