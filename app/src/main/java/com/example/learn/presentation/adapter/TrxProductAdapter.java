package com.example.learn.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.data.dto.trx.OrderItemDto;
import com.example.learn.helper.constant.ViewConst;
import com.example.learn.helper.utils.StringUtils;

import java.util.List;

public class TrxProductAdapter extends RecyclerView.Adapter<TrxProductAdapter.TrxProductViewHolder> {
    private List<OrderItemDto> trxProducts;
    private boolean isLoading = true;

    public TrxProductAdapter() {
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        notifyDataSetChanged();
    }

    public void setTrxProducts(List<OrderItemDto> trxProducts) {
        this.trxProducts = trxProducts;
        this.isLoading = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return isLoading ? ViewConst.VIEW_TYPE_SKELETON : ViewConst.VIEW_TYPE_DATA;
    }

    @NonNull
    @Override
    public TrxProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ViewConst.VIEW_TYPE_SKELETON) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.skeleton_trx_product, parent, false);
            return new TrxProductViewHolder(view, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trx_product, parent, false);
            return new TrxProductViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TrxProductViewHolder holder, int position) {
        if(isLoading) {
            return;
        }

        OrderItemDto trxProduct = trxProducts.get(position);

        Glide.with(holder.productImg.getContext())
            .load(trxProduct.product.image)
            .placeholder(R.drawable.img_placeholder)
            .into(holder.productImg);

        holder.productName.setText(trxProduct.product.name);
        holder.productAmount.setText(StringUtils.formatCurrency(trxProduct.product.price));
        holder.productQuantity.setText("(x" + String.valueOf(trxProduct.quantity) + ")");
    }

    @Override
    public int getItemCount() {
        if(isLoading) {
            return 2;
        }

        if (trxProducts == null) {
            return 0;
        }

        return trxProducts.size();
    }

    public static class TrxProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImg;
        public TextView productName, productAmount, productQuantity;

        public TrxProductViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if(viewType == ViewConst.VIEW_TYPE_SKELETON) {
                return;
            }

            productImg = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productAmount = itemView.findViewById(R.id.product_amount);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }
    }
}
