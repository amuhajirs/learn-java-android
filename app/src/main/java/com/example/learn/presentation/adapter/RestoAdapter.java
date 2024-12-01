package com.example.learn.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.data.dto.resto.CategoryDto;
import com.example.learn.data.dto.resto.RestaurantDto;
import com.example.learn.presentation.ui.resto_detail.RestoDetailActivity;

import java.util.List;

public class RestoAdapter extends RecyclerView.Adapter<RestoAdapter.RestoViewHolder> {
    private List<RestaurantDto> restos;
    private CategoryDto category;
    private final Context context;

    public RestoAdapter(Context context) {
        this.context = context;
    }

    public void setRestos(List<RestaurantDto> restos, CategoryDto category) {
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
        if (restos == null) {
            return;
        }

        RestaurantDto resto = restos.get(position);

        Glide.with(holder.restoImage.getContext()).load(resto.avatar).into(holder.restoImage);

        holder.restoName.setText(resto.name);
        holder.restoCategory.setText(category.name);
        holder.restoRating.setText(String.format("%.1f", resto.ratingAvg));
        holder.restoRatingCount.setText(String.format("(%d)", resto.ratingCount));

        holder.cardResto.setOnClickListener(v -> {
            Intent intent = new Intent(context, RestoDetailActivity.class);
            intent.putExtra("id", String.valueOf(resto.id));
            intent.putExtra("banner", resto.banner);
            intent.putExtra("avatar", resto.avatar);
            intent.putExtra("name", resto.name);
            intent.putExtra("category", category.name);
            intent.putExtra("rating_avg", resto.ratingAvg);
            intent.putExtra("rating_count", resto.ratingCount);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (restos == null) {
            return 0;
        }
        return restos.size();
    }

    public static class RestoViewHolder extends RecyclerView.ViewHolder {
        TextView restoName, restoCategory, restoRating, restoRatingCount;
        ImageView restoImage;
        CardView cardResto;

        public RestoViewHolder(@NonNull View itemView) {
            super(itemView);
            restoImage = itemView.findViewById(R.id.resto_avatar);
            restoName = itemView.findViewById(R.id.resto_name);
            restoCategory = itemView.findViewById(R.id.resto_category);
            restoRating = itemView.findViewById(R.id.resto_rating);
            restoRatingCount = itemView.findViewById(R.id.resto_rating_count);
            cardResto = (CardView) itemView;
        }
    }
}
