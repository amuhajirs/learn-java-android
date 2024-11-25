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
import com.example.learn.data.dto.resto.RestaurantsPerCategory;
import com.example.learn.domain.model.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryRestoAdapter extends RecyclerView.Adapter<CategoryRestoAdapter.CategoryRestoViewHolder> {
    private final List<RestaurantsPerCategory> restoCategories;
    private final Context context;

    public CategoryRestoAdapter(Context context, List<RestaurantsPerCategory> restoCategories) {
        this.context = context;
        this.restoCategories = restoCategories;
    }

    @NonNull
    @Override
    public CategoryRestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_resto, parent, false);
        return new CategoryRestoViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRestoViewHolder holder, int position) {
        if (restoCategories == null) {
            return;
        }

        Log.d("RestoRecycler HEIGHT", "restoRecycler Height: " + holder.restoRecycler.getHeight());

        RestaurantsPerCategory restoCategory = restoCategories.get(position);
        holder.categoryName.setText(restoCategory.category.name);

        ArrayList<Restaurant> restoList = new ArrayList<>();
        Collections.addAll(restoList, restoCategory.data);

        RestoAdapter restoAdapter = new RestoAdapter(context);
        restoAdapter.setRestos(restoList, restoCategory.category);

        holder.restoRecycler.setAdapter(restoAdapter);
        holder.restoRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.restoRecycler.setNestedScrollingEnabled(false);

//        holder.restoRecycler.setRecycledViewPool(viewPool); // Bagikan pool yang sama untuk semua inner RecyclerView
//        holder.restoRecycler.setAdapter(new RestoAdapter(category.getRestos())); // Isi data resto
    }

    @Override
    public int getItemCount() {
        if (restoCategories == null) {
            return 0;
        }
        return restoCategories.size();
    }

    public static class CategoryRestoViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        RecyclerView restoRecycler;

        public CategoryRestoViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            restoRecycler = itemView.findViewById(R.id.resto_recycler);
            Log.d("CategoryResto HEIGHT", "categoryResto Height: " + itemView.getHeight());
        }
    }
}