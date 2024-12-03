package com.example.learn.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.R;

public class TrxProductAdapter extends RecyclerView.Adapter<TrxProductAdapter.TrxProductViewHolder> {
    @NonNull
    @Override
    public TrxProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trx_product, parent, false);
        return new TrxProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrxProductViewHolder holder, int position) {
        Glide.with(holder.productImg.getContext())
            .load("http://202.179.191.73:8080/public/dummy/avatar-resto.png")
            .placeholder(R.drawable.img_placeholder)
            .into(holder.productImg);

        holder.productName.setText("Mie Ayam");
        holder.productAmount.setText("Rp 20.000");
        holder.productQuantity.setText("(x4)");
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public static class TrxProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImg;
        public TextView productName, productAmount, productQuantity;

        public TrxProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImg = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productAmount = itemView.findViewById(R.id.product_amount);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }
    }
}
