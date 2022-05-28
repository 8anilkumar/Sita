package com.eminence.sitasrm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.Activity.CancelSingleProductActivity;
import com.eminence.sitasrm.Activity.CancelledOrderActivity;
import com.eminence.sitasrm.Models.ProductOrderModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.YourPreference;

import java.util.ArrayList;

import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<ProductOrderModel> subcat;
    String odrstatus = "",od_id = "",from = "";


    public ProductOrderAdapter(ArrayList<ProductOrderModel> subcat, Context context,String od_id,String from) {
        this.subcat = subcat;
        this.context = context;
        this.od_id = od_id;
        this.from = from;

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_product_layout, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String language = yourPrefrence.getData("language");

//        if(from.equalsIgnoreCase("RequestCancel")){
//            holder.arrowLayout.setVisibility(View.GONE);
//        } else {
//            holder.arrowLayout.setVisibility(View.GONE);
//        }

        if (language.equalsIgnoreCase("hi")){
            holder.txt_productName.setText(subcat.get(position).getProductNameHindi() +" "+ subcat.get(position).getP_caption_hindi());
            holder.txt_discount.setText( "मात्रा: "+subcat.get(position).getQuantity());

            if(subcat.get(position).getOdrStatus().equalsIgnoreCase("1")) {
                odrstatus = "ऑर्डर किया गया :";
            } else {
                odrstatus = "ऑर्डर रद्द किया गया :";
            }
        } else {
            holder.txt_productName.setText(subcat.get(position).getProductName()+" "+ subcat.get(position).getProduct_caption());
            holder.txt_discount.setText("Ouantity: "+subcat.get(position).getQuantity());
            if(subcat.get(position).getOdrStatus().equalsIgnoreCase("1")) {
                odrstatus = "Order Place On :";
            } else {
                odrstatus = "Order Cancelled On :";
            }
        }

        holder.txt_createdAt.setText(odrstatus+subcat.get(position).getCreatedAt());
        holder.txt_price.setText("₹"+subcat.get(position).getPrice());

        Glide.with(context).load(imagebaseurl + subcat.get(position).getProductImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo))
                .into(holder.product_image);

        holder.singleProductLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(from.equalsIgnoreCase("RequestCancel")) {
//
//                } else {
//                    if (subcat.get(position).getOdrStatus().equalsIgnoreCase("0")) {
//                        Intent intent = new Intent(context, CancelledOrderActivity.class);
//                        intent.putExtra("order_id", od_id);
//                        intent.putExtra("product_id", subcat.get(position).getProductId());
//                        intent.putExtra("quantity", subcat.get(position).getQuantity());
//                        intent.putExtra("name", subcat.get(position).getProductName());
//                        intent.putExtra("name_hindi", subcat.get(position).getProductNameHindi());
//                        intent.putExtra("price", "₹" + subcat.get(position).getPrice());
//                        intent.putExtra("product_image", subcat.get(position).getProductImage());
//                        context.startActivity(intent);
//
//                    } else {
//                        Intent intent = new Intent(context, CancelSingleProductActivity.class);
//                        intent.putExtra("order_id", od_id);
//                        intent.putExtra("created_at", subcat.get(position).getCreatedAt());
//                        intent.putExtra("product_id", subcat.get(position).getProductId());
//                        intent.putExtra("quantity", subcat.get(position).getQuantity());
//                        intent.putExtra("name", subcat.get(position).getProductName());
//                        intent.putExtra("name_hindi", subcat.get(position).getProductNameHindi());
//                        intent.putExtra("price", "₹" + subcat.get(position).getPrice());
//                        intent.putExtra("product_image", subcat.get(position).getProductImage());
//                        context.startActivity(intent);
//                    }
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_createdAt, txt_productName, txt_discount, txt_price;
        private ImageView product_image;
        private LinearLayout singleProductLayout,arrowLayout;

        public MyViewHolder(View view) {
            super(view);
            txt_createdAt = view.findViewById(R.id.txt_createdAt);
            txt_productName = view.findViewById(R.id.txt_productName);
            txt_discount = view.findViewById(R.id.txt_discount);
            txt_price = view.findViewById(R.id.txt_price);
            product_image = view.findViewById(R.id.product_image);
            singleProductLayout = view.findViewById(R.id.singleProductLayout);
            arrowLayout = view.findViewById(R.id.arrowLayout);

        }
    }

}


