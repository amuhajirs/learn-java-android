package com.example.learn.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.data.dto.trx.OrderDto;
import com.example.learn.helper.constant.ViewConst;
import com.example.learn.helper.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

public class TrxRestoAdapter extends RecyclerView.Adapter<TrxRestoAdapter.TrxRestoViewHolder> {
    private Context context;
    private List<OrderDto> transactions;
    private boolean isLoading = true;

    public TrxRestoAdapter(Context context) {
        this.context = context;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        notifyDataSetChanged();
    }

    public void setTransaction(List<OrderDto> transactions) {
        this.transactions = transactions;
        this.isLoading = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return isLoading ? ViewConst.VIEW_TYPE_SKELETON : ViewConst.VIEW_TYPE_DATA;
    }

    @NonNull
    @Override
    public TrxRestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ViewConst.VIEW_TYPE_SKELETON) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.skeleton_card_trx_resto, parent, false);
            return new TrxRestoViewHolder(view, context, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_trx_resto, parent, false);
            return new TrxRestoViewHolder(view, context, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TrxRestoViewHolder holder, int position) {
        TrxProductAdapter trxProductAdapter = new TrxProductAdapter();
        holder.trxProductRecycler.setAdapter(trxProductAdapter);

        if(isLoading) {
            trxProductAdapter.setLoading(isLoading);
            return;
        }

        if (trxProductAdapter == null) {
            return;
        }

        OrderDto trx = transactions.get(position);

        Glide.with(holder.restoAvatar.getContext())
                .load(trx.restaurant.avatar)
                .placeholder(R.drawable.img_placeholder)
                .into(holder.restoAvatar);

        holder.restoName.setText(trx.restaurant.name);
        holder.totalAmount.setText(StringUtils.formatCurrency(trx.amount));
        holder.trxId.setText(trx.transaction.transactionCode);

        trxProductAdapter.setTrxProducts(Arrays.asList(trx.items));
    }

    @Override
    public int getItemCount() {
        if(isLoading) {
            return 5;
        }

        if (transactions == null) {
            return 0;
        }

        return transactions.size();
    }

    public static class TrxRestoViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView trxProductRecycler;
        public ImageView restoAvatar;
        public TextView restoName, totalAmount, trxId;

        public TrxRestoViewHolder(@NonNull View itemView, Context context, int viewType) {
            super(itemView);

            trxProductRecycler = itemView.findViewById(R.id.trx_product_recycler);
            trxProductRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            trxProductRecycler.setNestedScrollingEnabled(false);

            if (viewType == ViewConst.VIEW_TYPE_SKELETON) {
                return;
            }

            restoAvatar = itemView.findViewById(R.id.resto_avatar);
            restoName = itemView.findViewById(R.id.resto_name);
            totalAmount = itemView.findViewById(R.id.total_amount);
            trxId = itemView.findViewById(R.id.trx_id);

        }
    }
}
