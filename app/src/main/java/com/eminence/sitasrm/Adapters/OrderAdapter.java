package com.eminence.sitasrm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eminence.sitasrm.Activity.OrderDetails;
import com.eminence.sitasrm.Models.OrderModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

import java.util.ArrayList;
import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderModel> subcat;
    private ArrayList<OrderModel> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();

    public OrderAdapter(ArrayList<OrderModel> subcat, Context context) {
        this.subcat = subcat;
        this.context = context;
        this.filteredData = subcat;
      }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.packed.setText("Order Status : "+filteredData.get(position).getOrder_status());

        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String language = yourPrefrence.getData("language");
        if (language.equalsIgnoreCase("hi") || language.equalsIgnoreCase("")) {
            holder.orderdatetime.setText("ऑर्डर ऑन "+filteredData.get(position).getCreated_at());
            holder.orderid.setText("ऑर्डर आईडी: "+""+filteredData.get(position).getOrder_id());
        } else {
            holder.orderdatetime.setText("Order On "+filteredData.get(position).getCreated_at());
            holder.orderid.setText("Order ID: "+""+filteredData.get(position).getOrder_id());
        }

        holder.orderList_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, OrderDetails.class);
                intent.putExtra("order_id", subcat.get(position).getOrder_id());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {

        return filteredData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderdatetime, orderid, packed;
        ImageView product_image;
        LinearLayout orderList_layout;


        public MyViewHolder(View view) {
            super(view);
            orderid = view.findViewById(R.id.orderid);
            orderdatetime = view.findViewById(R.id.orderdatetime);
            packed = view.findViewById(R.id.packed);
            orderList_layout = view.findViewById(R.id.orderList_layout);

        }
    }


    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<OrderModel> list = subcat;
            int count = list.size();
            final ArrayList<OrderModel> nlist = new ArrayList<>(count);
            String filterableText = null;
            if (!filterString.equals("")) {
                for (OrderModel model : list) {
                    if(!filterString.equals("")) {
                        filterableText = model.getCreated_at()+model.getOrder_id();
                        if (filterableText != null && filterableText.toLowerCase().contains(filterString))  {
                            nlist.add(model);
                        }
                    } else {
                        nlist.add(model);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            } else {
                results.values = subcat;
                results.count = subcat.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<OrderModel>) results.values;
            notifyDataSetChanged();
        }
    }

}


