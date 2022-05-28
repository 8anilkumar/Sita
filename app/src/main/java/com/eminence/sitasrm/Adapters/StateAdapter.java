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

import com.eminence.sitasrm.Interface.StateListner;
import com.eminence.sitasrm.Models.StateModel;
import com.eminence.sitasrm.R;
import java.util.ArrayList;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

    Context context;
    ArrayList<StateModel> statelist;
    StateListner stateListner;
    AlertDialog alertDialog;

    public StateAdapter(ArrayList<StateModel> statelist, Context context,StateListner stateListner,AlertDialog alertDialog) {
        this.statelist = statelist;
        this.context = context;
        this.stateListner = stateListner;
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

        holder.txtState.setText(statelist.get(position).getStateName());
        holder.stateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateListner.stateListner(statelist.get(position).getStateId(),statelist.get(position).getStateName());
                 alertDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return statelist.size();
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

