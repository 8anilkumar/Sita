package com.eminence.sitasrm.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eminence.sitasrm.Interface.CityListner;
import com.eminence.sitasrm.Models.CityModel;
import com.eminence.sitasrm.R;
import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    Context context;
    ArrayList<CityModel> citylist;
    CityListner cityListner;
    AlertDialog alertDialog;

    public CityAdapter(ArrayList<CityModel> citylist, Context context,CityListner cityListner,AlertDialog alertDialog) {
        this.citylist = citylist;
        this.context = context;
        this.cityListner = cityListner;
        this.alertDialog = alertDialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txtState.setText(citylist.get(position).getCityName());
        holder.stateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityListner.cityListner(citylist.get(position).getCityId(),citylist.get(position).getCityName());
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return citylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout stateLayout;
        TextView txtState;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stateLayout = itemView.findViewById(R.id.stateLayout);
            txtState = itemView.findViewById(R.id.txtState);

        }
    }
}
