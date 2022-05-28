package com.eminence.sitasrm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eminence.sitasrm.Models.DashboardModel;
import com.eminence.sitasrm.R;
import java.util.ArrayList;


public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {
    Context context;
    ArrayList<DashboardModel> subcat;
    String category, type;

    public DashboardAdapter(ArrayList<DashboardModel> subcat, Context context, String type) {
        this.subcat = subcat;
        this.context = context;
        this.category = category;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactionlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.point.setText(subcat.get(position).getAmount());
        holder.discription.setText(subcat.get(position).getDescription());
        holder.title.setText(subcat.get(position).getType());
        holder.date.setText(subcat.get(position).getCreated_date()+"\n\n"+subcat.get(position).getCreated_time());

//        if (subcat.get(position).getSource().equalsIgnoreCase("Redeemed")) {
//            holder.title.setText(subcat.get(position).getSource());
//            holder.point.setText("-" + subcat.get(position).getPoints() + " Points");
//            holder.discription.setText("10X Champions Offer");
//
//        } else if (subcat.get(position).getSource().equalsIgnoreCase("QR Scan")) {
//            holder.title.setText("Won");
//            holder.point.setText("+" + subcat.get(position).getPoints() + " Points");
//            holder.discription.setText("Scan Of QR");
//
//        } else {
//            holder.title.setText(subcat.get(position).getSource());
//            holder.point.setText("+" + subcat.get(position).getPoints() + " Points");
//            holder.discription.setText(subcat.get(position).getSource());
//        }

    }

    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView point, date, title, discription;
        LinearLayout call;

        public MyViewHolder(View view) {
            super(view);
            point = view.findViewById(R.id.point);
            date = view.findViewById(R.id.date);
            title = view.findViewById(R.id.title);
            discription = view.findViewById(R.id.discription);
        }
    }
}


