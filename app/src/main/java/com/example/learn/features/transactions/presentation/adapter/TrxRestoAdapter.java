package com.example.learn.features.transactions.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learn.R;
import com.example.learn.features.transactions.data.dto.OrderDto;
import com.example.learn.common.constant.ViewConst;
import com.example.learn.common.utils.StringUtils;

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

        switch (trx.status) {
            case WAITING:
                holder.statusText.setText("Menunggu Konfirmasi");
                holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.blue));
                holder.statusCard.setCardBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(context, R.color.blue), (int) (0.2 * 255)));
                break;
            case INPROGRESS:
                holder.statusText.setText("Diproses");
                holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.orange));
                holder.statusCard.setCardBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(context, R.color.orange), (int) (0.2 * 255)));
                break;
            case READY:
                holder.statusText.setText("Siap Diambil");
                holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.orange));
                holder.statusCard.setCardBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(context, R.color.orange), (int) (0.2 * 255)));
                break;
            case SUCCESS:
                holder.statusText.setText("Sukses");
                holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.statusCard.setCardBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(context, R.color.green), (int) (0.2 * 255)));
                break;
            case CANCELED:
                holder.statusText.setText("Dibatalkan");
                holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.statusCard.setCardBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(context, R.color.red), (int) (0.2 * 255)));
                break;
        }

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
        public TextView restoName, totalAmount, trxId, statusText;
        public CardView statusCard;

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
            statusText = itemView.findViewById(R.id.status_text);
            statusCard = itemView.findViewById(R.id.status_card);
        }
    }
}
