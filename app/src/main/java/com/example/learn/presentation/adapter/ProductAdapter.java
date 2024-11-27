package com.example.learn.presentation.adapter;

import android.content.Context;
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
import com.example.learn.domain.model.Category;
import com.example.learn.domain.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Category category;

    public void setProducts(List<Product> products, Category category) {
        this.products = products;
        this.category = category;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (products == null) {
            return;
        }

        Product product = products.get(position);

        Glide.with(holder.productImage.getContext()).load(product.image).into(holder.productImage);
        holder.productName.setText(product.name);
        holder.productPrice.setText(category.name);
        holder.productSold.setText(String.valueOf(product.sold));
        holder.productLike.setText(String.valueOf(product.like));
    }

    @Override
    public int getItemCount() {
        if (products == null) {
            return 0;
        }
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productSold, productLike;
        CardView cardProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productSold = itemView.findViewById(R.id.product_sold);
            productLike = itemView.findViewById(R.id.product_like);
            cardProduct = (CardView) itemView;
        }
    }
}
