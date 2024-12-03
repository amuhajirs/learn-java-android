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

public class TrxRestoAdapter extends RecyclerView.Adapter<TrxRestoAdapter.TrxRestoViewHolder> {
    private Context context;

    public TrxRestoAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TrxRestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_trx_resto, parent, false);
        return new TrxRestoViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TrxRestoViewHolder holder, int position) {
        Glide.with(holder.restoAvatar.getContext())
                .load("http://202.179.191.73:8080/public/dummy/avatar-resto.png")
                .placeholder(R.drawable.img_placeholder)
                .into(holder.restoAvatar);

        holder.restoName.setText("Resto Ridwan");
        holder.totalAmount.setText("Rp 20.000");
        holder.trxId.setText("EC-01");

        TrxProductAdapter trxProductAdapter = new TrxProductAdapter();
        holder.trxProductRecycler.setAdapter(trxProductAdapter);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class TrxRestoViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView trxProductRecycler;
        public ImageView restoAvatar;
        public TextView restoName, totalAmount, trxId;

        public TrxRestoViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            trxProductRecycler = itemView.findViewById(R.id.trx_product_recycler);
            restoAvatar = itemView.findViewById(R.id.resto_avatar);
            restoName = itemView.findViewById(R.id.resto_name);
            totalAmount = itemView.findViewById(R.id.total_amount);
            trxId = itemView.findViewById(R.id.trx_id);

            trxProductRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            trxProductRecycler.setNestedScrollingEnabled(false);
        }
    }
}
