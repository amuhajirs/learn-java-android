package com.example.learn.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.learn.R;
import com.example.learn.data.dto.resto.ProductDto;
import com.example.learn.helper.constant.ViewConst;
import com.example.learn.helper.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context context;
    private final QuantitiesViewModel viewModel;
    private final OnClickProduct onClickProduct;
    private List<ProductDto> products;
    private boolean isLoading = true;

    public ProductAdapter(Context context, QuantitiesViewModel viewModel, OnClickProduct onClickProduct) {
        this.context = context;
        this.viewModel = viewModel;
        this.onClickProduct = onClickProduct;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        notifyDataSetChanged();
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
        this.isLoading = false;

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return isLoading ? ViewConst.VIEW_TYPE_SKELETON : ViewConst.VIEW_TYPE_DATA;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ViewConst.VIEW_TYPE_SKELETON) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.skeleton_card_product, parent, false);
            return new ProductViewHolder(view, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_product, parent, false);
            return new ProductViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (isLoading) {
            return;
        }

        if (products == null) {
            return;
        }

        ProductDto product = products.get(position);

        Glide.with(holder.productImage.getContext())
                .load(product.image)
                .placeholder(R.drawable.img_placeholder)
                .into(holder.productImage);
        holder.productName.setText(product.name);
        holder.productPrice.setText(StringUtils.formatCurrency(product.price));
        holder.productSold.setText(String.valueOf(product.sold));
        holder.productLike.setText(String.valueOf(product.like));
        holder.quantity.setText(viewModel.getQuantities().getValue().getOrDefault(product.id, 0).toString());

        if(product.stock == 0) {
            holder.btnProduct.setVisibility(View.GONE);
            holder.emptyStock.setVisibility(View.VISIBLE);
        }

        holder.cardProduct.setOnClickListener(v -> {
            onClickProduct.onItemClick(product);
        });

        viewModel.getQuantities().observe((LifecycleOwner) context, quantities -> {
            int currentQty = quantities.getOrDefault(product.id, 0);
            holder.quantity.setText(String.valueOf(currentQty));
        });

        holder.incBtn.setOnClickListener(v -> {
            int currentQty = viewModel.getQuantities().getValue().getOrDefault(product.id, 0);
            if (currentQty < product.stock) {
                viewModel.updateQuantity(product.id, currentQty + 1);
            } else {
                Toast.makeText(context, "Stok tidak cukup", Toast.LENGTH_SHORT).show();
            }
        });

        holder.decBtn.setOnClickListener(v -> {
            int currentQty = viewModel.getQuantities().getValue().getOrDefault(product.id, 0);
            if (currentQty > 0) {
                viewModel.updateQuantity(product.id, currentQty - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(isLoading) {
            return 3;
        }

        if (products == null) {
            return 0;
        }
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productSold, productLike, emptyStock, quantity;
        LinearLayout btnProduct;
        CardView cardProduct;
        ImageButton incBtn, decBtn;

        public ProductViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if(viewType == ViewConst.VIEW_TYPE_SKELETON) {
                return;
            }

            productImage = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productSold = itemView.findViewById(R.id.product_sold);
            productLike = itemView.findViewById(R.id.product_like);
            emptyStock = itemView.findViewById(R.id.empty_stock);
            btnProduct = itemView.findViewById(R.id.btn_product);
            incBtn = itemView.findViewById(R.id.inc_btn);
            decBtn = itemView.findViewById(R.id.dec_btn);
            quantity = itemView.findViewById(R.id.quantity);
            cardProduct = (CardView) itemView;
        }
    }

    public interface OnClickProduct {
        public void onItemClick(ProductDto product);
    }

    public interface QuantitiesViewModel {
        public LiveData<Map<Integer, Integer>> getQuantities();
        void updateQuantity(int productId, int quantity);
    }
}
