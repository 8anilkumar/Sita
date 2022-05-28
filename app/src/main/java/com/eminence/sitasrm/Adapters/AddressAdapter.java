package com.eminence.sitasrm.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eminence.sitasrm.Activity.Profile.AddAdress;
import com.eminence.sitasrm.Interface.DeleteAddressListner;
import com.eminence.sitasrm.Interface.MakeDefaultAddressListner;
import com.eminence.sitasrm.Models.AddressModel;
import com.eminence.sitasrm.R;

import java.util.ArrayList;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {
    Context context;
    ArrayList<AddressModel> subcat;
    String category;
    MakeDefaultAddressListner makeDefaltAddress;
    DeleteAddressListner deleteAddressListner;

    public AddressAdapter(ArrayList<AddressModel> subcat, Context context, MakeDefaultAddressListner makeDefaltAddress,DeleteAddressListner deleteAddressListner) {
        this.subcat = subcat;
        this.context = context;
        this.category = category;
        this.makeDefaltAddress = makeDefaltAddress;
        this.deleteAddressListner = deleteAddressListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.address.setText(subcat.get(position).getHf_number() + " " + subcat.get(position).getAddress() + " " + subcat.get(position).getLandmark() + "\n" + subcat.get(position).getState() + " " + subcat.get(position).getPincode());

        String type = subcat.get(position).getType();
        holder.type.setText(type + " Address");
        if (subcat.get(position).getDefault_address().equalsIgnoreCase("1")) {
            holder.txt_default.setVisibility(View.VISIBLE);
            holder.cart_AddressLayout.setBackground(context.getResources().getDrawable(R.drawable.address_border));
        }

        if (type.equalsIgnoreCase("Home")) {
            holder.image.setImageResource(R.drawable.ic_baseline_home_24);
        } else if (type.equalsIgnoreCase("Work")) {
            holder.image.setImageResource(R.drawable.ic_baseline_work_24);
        } else {
            holder.image.setImageResource(R.drawable.ic_baseline_location_on_24);
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddressListner.addressListner(subcat.get(position).getId());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddAdress.class);
                intent.putExtra("intenttype", "edit");
                intent.putExtra("id", subcat.get(position).getId());
                intent.putExtra("houseno", subcat.get(position).getHf_number());
                intent.putExtra("address", subcat.get(position).getAddress());
                intent.putExtra("landmark", subcat.get(position).getLandmark());
                intent.putExtra("state", subcat.get(position).getState());
                intent.putExtra("pincode", subcat.get(position).getPincode());
                intent.putExtra("type", subcat.get(position).getType());
                intent.putExtra("name", subcat.get(position).getName());
                intent.putExtra("mobile1", subcat.get(position).getMobile1());
                intent.putExtra("mobile2", subcat.get(position).getMobile2());
                intent.putExtra("default", subcat.get(position).getDefault_address());
                context.startActivity(intent);
                //((Activity) context).finish();

            }
        });

        holder.cart_AddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subcat.size() > 1) {
                    makeDefaltAddress.getDefalt(subcat.get(position).getId());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView address, edit, type, txt_default;
        ImageView image, delete;
        LinearLayout layout;
        Button redeemnow;
        CardView cart_AddressLayout;

        public MyViewHolder(View view) {
            super(view);
            address = view.findViewById(R.id.txt_address);
            edit = view.findViewById(R.id.txt_editAddress);
            type = view.findViewById(R.id.txt_addressType);
            image = view.findViewById(R.id.imgAddress);
            delete = view.findViewById(R.id.deleteImage);
            txt_default = view.findViewById(R.id.txt_default);
            cart_AddressLayout = view.findViewById(R.id.cart_AddressLayout);


        }
    }
}


