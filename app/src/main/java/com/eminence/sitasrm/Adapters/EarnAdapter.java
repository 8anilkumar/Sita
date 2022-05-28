package com.eminence.sitasrm.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eminence.sitasrm.Activity.Profile.Scratch_coupon;
import com.eminence.sitasrm.Models.Earnedmodel;
import com.eminence.sitasrm.R;
import java.util.ArrayList;

public class EarnAdapter extends RecyclerView.Adapter<EarnAdapter.MyViewHolder> {
    Context context;
    ArrayList<Earnedmodel> subcat;
    String category,type;
    AlertDialog deleteDialog;

    public EarnAdapter(ArrayList<Earnedmodel> subcat, Context context, String type) {
        this.subcat = subcat;
        this.context = context;
        this.category = category;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.points, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
       // holder.couponLayout.setVisibility(View.GONE);
        holder.scratchCardLayout.setVisibility(View.GONE);
        holder.point.setText(subcat.get(position).getAmount());

        if (subcat.get(position).getRedeem_status().equalsIgnoreCase("1")) {
            holder.couponLayout.setVisibility(View.VISIBLE);
        } else {
            holder.scratchCardLayout.setVisibility(View.VISIBLE);
        }

        holder.couponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Scratch_coupon.class);
                intent.putExtra("point",subcat.get(position).getAmount());
                intent.putExtra("id",subcat.get(position).getId());
                intent.putExtra("redeem_status",subcat.get(position).getRedeem_status());
                intent.putExtra("date",subcat.get(position).getCreated_date());
                intent.putExtra("time",subcat.get(position).getCreated_time());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView point;
        LinearLayout couponLayout,scratchCardLayout;

        public MyViewHolder(View view) {
            super(view);
            point = view.findViewById(R.id.point);
            couponLayout = view.findViewById(R.id.couponLayout);
            scratchCardLayout = view.findViewById(R.id.scratchCardLayout);

        }
    }


}


