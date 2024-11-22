package com.example.test.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.R;
import com.example.test.domain.model.Category;
import com.example.test.domain.model.Restaurant;

import java.util.List;

public class RestoAdapter extends RecyclerView.Adapter<RestoAdapter.RestoViewHolder> {
    private List<Restaurant> restos;
    private Category category;

    public void setRestos(List<Restaurant> restos, Category category) {
        this.restos = restos;
        this.category = category;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_resto, parent, false);
        return new RestoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoViewHolder holder, int position) {
        if(restos == null) {
            return;
        }

        Restaurant resto = restos.get(position);

        Glide.with(holder.restoImage.getContext()).load(resto.avatar).into(holder.restoImage);

        holder.restoName.setText(resto.name);
        holder.restoCategory.setText(category.name);
        holder.restoRating.setText(String.format("%.1f", resto.ratingAvg));
        holder.restoRatingCount.setText(String.format("(%d)", resto.ratingCount));
    }

    @Override
    public int getItemCount() {
        if(restos == null) {
            return 0;
        }
        return restos.size();
    }

    public static class RestoViewHolder extends RecyclerView.ViewHolder {
        TextView restoName, restoCategory, restoRating, restoRatingCount;
        ImageView restoImage;

        public RestoViewHolder(@NonNull View itemView) {
            super(itemView);
            restoImage = itemView.findViewById(R.id.resto_img);
            restoName = itemView.findViewById(R.id.resto_name);
            restoCategory = itemView.findViewById(R.id.resto_category);
            restoRating = itemView.findViewById(R.id.resto_rating);
            restoRatingCount = itemView.findViewById(R.id.resto_rating_count);
        }
    }
}
