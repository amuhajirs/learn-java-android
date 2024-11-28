package com.example.learn.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import com.example.learn.R;
import com.example.learn.domain.model.Category;
import com.example.learn.domain.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    OnClickProduct onClickProduct;
    Context context;

    public void setProducts(List<Product> products, OnClickProduct onClickProduct, Context context) {
        this.products = products;
        this.onClickProduct = onClickProduct;
        this.context = context;
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

        Glide.with(holder.productImage.getContext())
                .load(product.image)
                .into(holder.productImage);
        holder.productName.setText(product.name);
        holder.productPrice.setText("Rp " + (new DecimalFormat("#,###")).format(product.price).replace(",", "."));
        holder.productSold.setText(String.valueOf(product.sold));
        holder.productLike.setText(String.valueOf(product.like));

        if(product.stock == 0) {
            holder.btnProduct.setVisibility(View.GONE);
            holder.emptyStock.setVisibility(View.VISIBLE);
        }

        holder.cardProduct.setOnClickListener(v -> {
            onClickProduct.onItemClick(product);
        });
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
        TextView productName, productPrice, productSold, productLike, emptyStock;
        LinearLayout btnProduct;
        CardView cardProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productSold = itemView.findViewById(R.id.product_sold);
            productLike = itemView.findViewById(R.id.product_like);
            emptyStock = itemView.findViewById(R.id.empty_stock);
            btnProduct = itemView.findViewById(R.id.btn_product);
            cardProduct = (CardView) itemView;
        }
    }

    public interface OnClickProduct {
        public void onItemClick(Product product);
    }
}
