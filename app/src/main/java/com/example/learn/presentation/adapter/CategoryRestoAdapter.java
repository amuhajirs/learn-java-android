package com.example.learn.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.data.dto.resto.RestaurantDto;
import com.example.learn.data.dto.resto.RestaurantsPerCategory;
import com.example.learn.helper.constant.ViewConst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryRestoAdapter extends RecyclerView.Adapter<CategoryRestoAdapter.CategoryRestoViewHolder> {
    private final Context context;
    private List<RestaurantsPerCategory> restoCategories;
    private boolean isLoading = true;

    public CategoryRestoAdapter(Context context) {
        this.context = context;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        notifyDataSetChanged();
    }

    public void setRestoCategories(List<RestaurantsPerCategory> restoCategories) {
        this.restoCategories = restoCategories;
        this.isLoading = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return isLoading ? ViewConst.VIEW_TYPE_SKELETON : ViewConst.VIEW_TYPE_DATA;
    }

    @NonNull
    @Override
    public CategoryRestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ViewConst.VIEW_TYPE_SKELETON) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.skeleton_category_resto, parent, false);
            return new CategoryRestoViewHolder(view, context, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_resto, parent, false);
            return new CategoryRestoViewHolder(view, context, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRestoViewHolder holder, int position) {
        RestoAdapter restoAdapter = new RestoAdapter(context);
        holder.restoRecycler.setAdapter(restoAdapter);

        if(isLoading) {
            restoAdapter.setLoading(isLoading);
            return;
        }

        if (restoCategories == null) {
            return;
        }

        RestaurantsPerCategory restoCategory = restoCategories.get(position);
        holder.categoryName.setText(restoCategory.category.name);

        restoAdapter.setRestos(Arrays.asList(restoCategory.data), restoCategory.category);
    }

    @Override
    public int getItemCount() {
        if(isLoading) {
            return 3;
        }

        if (restoCategories == null) {
            return 0;
        }

        return restoCategories.size();
    }

    public static class CategoryRestoViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView restoRecycler;

        public CategoryRestoViewHolder(@NonNull View itemView, Context context, int viewType) {
            super(itemView);

            restoRecycler = itemView.findViewById(R.id.resto_recycler);

            restoRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            restoRecycler.setNestedScrollingEnabled(false);

            if(viewType == ViewConst.VIEW_TYPE_SKELETON) {
                return;
            }

            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
