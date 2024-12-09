package com.example.learn.features.resto.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.common.constant.ViewConst;
import com.example.learn.features.resto.data.dto.ProductsPerCategory;

import java.util.Arrays;
import java.util.List;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder> {
    private final Context context;
    private final ProductAdapter.QuantitiesViewModel viewModel;
    private final ProductAdapter.OnClickProduct onClickProduct;
    private List<ProductsPerCategory> productCategories;
    private boolean isLoading = true;

    public CategoryProductAdapter(Context context, ProductAdapter.QuantitiesViewModel viewModel, ProductAdapter.OnClickProduct onClickProduct) {
        this.context = context;
        this.viewModel = viewModel;
        this.onClickProduct = onClickProduct;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        notifyDataSetChanged();
    }

    public void setProductCategories(List<ProductsPerCategory> productCategories) {
        this.productCategories = productCategories;
        this.isLoading = false;

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return isLoading ? ViewConst.VIEW_TYPE_SKELETON : ViewConst.VIEW_TYPE_DATA;
    }

    @NonNull
    @Override
    public CategoryProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ViewConst.VIEW_TYPE_SKELETON) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.skeleton_category_product, parent, false);
            return new CategoryProductViewHolder(view, context, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_product, parent, false);
            return new CategoryProductViewHolder(view, context, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryProductViewHolder holder, int position) {
        ProductAdapter productAdapter = new ProductAdapter(context, viewModel, onClickProduct);
        holder.productRecycler.setAdapter(productAdapter);

        if (isLoading) {
            productAdapter.setLoading(isLoading);
            return;
        }

        if (productCategories == null) {
            return;
        }

        ProductsPerCategory productCategory = productCategories.get(position);
        holder.categoryName.setText(productCategory.category.name);

        productAdapter.setProducts(Arrays.asList(productCategory.data));
    }

    @Override
    public int getItemCount() {
        if (isLoading) {
            return 3;
        }

        if (productCategories == null) {
            return 0;
        }
        return productCategories.size();
    }

    public static class CategoryProductViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView productRecycler;

        public CategoryProductViewHolder(@NonNull View itemView, Context context, int viewType) {
            super(itemView);

            productRecycler = itemView.findViewById(R.id.product_recycler);

            productRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            productRecycler.setNestedScrollingEnabled(false);

            if (viewType == ViewConst.VIEW_TYPE_SKELETON) {
                return;
            }

            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
