package com.eminence.sitasrm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eminence.sitasrm.Activity.OrderDetails;
import com.eminence.sitasrm.Activity.RewardPointActivity;
import com.eminence.sitasrm.Models.TransactionsModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    Context context;
    ArrayList<TransactionsModel> subcat;

    public TransactionAdapter(ArrayList<TransactionsModel> subcat, Context context) {
        this.subcat = subcat;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.datetxt.setText(subcat.get(position).getCreated_date());
        holder.time.setText(subcat.get(position).getCreated_time());
        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String language = yourPrefrence.getData("language");
        if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
            holder.ref_id.setText(subcat.get(position).getDescription_hindi());
        } else {
            holder.ref_id.setText(subcat.get(position).getDescription());
        }

        if (subcat.get(position).getType().equalsIgnoreCase("Deduct")) {
            if(subcat.get(position).getAmount().equalsIgnoreCase("0")) {
                holder.transactionLayout.setVisibility(View.GONE);
            } else {
                holder.txt_Type.setText("\u20B9" + "-" + subcat.get(position).getAmount());
                holder.txt_Type.setTextColor(Color.parseColor("#ff0000"));
            }
        } else {
            holder.txt_Type.setText("\u20B9"+"+" + subcat.get(position).getAmount());
            holder.txt_Type.setTextColor(Color.parseColor("#68C36C"));
        }

        holder.transactionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subcat.get(position).getType().equalsIgnoreCase("Deduct")){
                    String orderID = subcat.get(position).getId();
                    Intent intent = new Intent(context, OrderDetails.class);
                    intent.putExtra("order_id", orderID);
                    context.startActivity(intent);
                } else if(subcat.get(position).getType().equalsIgnoreCase("Scratch Card Redeem")){
                    Intent intent = new Intent(context, RewardPointActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ref_id, datetxt, time, txt_Type;
        public LinearLayout transactionLayout;

        public MyViewHolder(View view) {
            super(view);
            ref_id = view.findViewById(R.id.ref_id);
            datetxt = view.findViewById(R.id.datetxt);
            time = view.findViewById(R.id.time);
            transactionLayout = view.findViewById(R.id.transactionLayout);
            txt_Type = view.findViewById(R.id.txt_Type);

        }
    }
}


