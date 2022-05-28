package com.eminence.sitasrm.Adapters;

import android.content.Context;
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
import com.eminence.sitasrm.Models.WinnerModel;
import com.eminence.sitasrm.R;
import java.util.ArrayList;
import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

public class WinnerAdapter extends RecyclerView.Adapter<WinnerAdapter.MyViewHolder> {
    Context context;
    ArrayList<WinnerModel> subcat;
    String category,type;

    public WinnerAdapter(ArrayList<WinnerModel> subcat, Context context) {
        this.subcat = subcat;
        this.context = context;
        this.category = category;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.winnerlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(subcat.get(position).getName());
        holder.productname.setText(subcat.get(position).getProduct_name());
        //holder.points.setText(subcat.get(position).getRedeemed_on()+" Points");

        Glide.with(context).load(imagebaseurl+subcat.get(position).getProfile_pic())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading)
                        .centerCrop()
                        .error(R.drawable.loading))
                .into(holder.profileimage);

        Glide.with(context).load(imagebaseurl+subcat.get(position).getProduct_image())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading))
                .into(holder.productimage);
    }

    @Override
    public int getItemCount() {
        return subcat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,productname,points;
        ImageView profileimage,productimage,favorite;
        LinearLayout call;

        public MyViewHolder(View view) {
            super(view);
            profileimage = view.findViewById(R.id.profileimage);
            name = view.findViewById(R.id.name);
            productname = view.findViewById(R.id.productname);
            productimage = view.findViewById(R.id.productimage);
            points = view.findViewById(R.id.points);

        }
    }
}


