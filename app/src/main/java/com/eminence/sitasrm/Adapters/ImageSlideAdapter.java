package com.eminence.sitasrm.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.Activity.ProductDetails;
import com.eminence.sitasrm.Models.Images;
import com.eminence.sitasrm.R;
import com.github.demono.adapter.InfinitePagerAdapter;

import java.util.List;

import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;


public class ImageSlideAdapter extends InfinitePagerAdapter {

    private List<Images> data;
    private Context context;

    public ImageSlideAdapter(List<Images> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public ImageSlideAdapter(List<Images> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.img_item, container, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_item);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, ProductDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("productid", data.get(position).getProduct_id());
                context.startActivity(intent);
            }
        });

        Glide.with(context).load(imagebaseurl+data.get(position).getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading)
                        )
                .into(imageView);

        return convertView;
    }

}