package com.eminence.sitasrm.Adapters;

import static com.eminence.sitasrm.Utils.Baseurl.imagebaseurl;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eminence.sitasrm.Models.Feedbackmodel;
import com.eminence.sitasrm.R;
import com.github.demono.adapter.InfinitePagerAdapter;

import java.util.ArrayList;

public class FeedbackSliderAdapter extends InfinitePagerAdapter {
    Context context;
    ArrayList<Feedbackmodel> subcat;
    String category;

    public FeedbackSliderAdapter(ArrayList<Feedbackmodel> subcat, Context context) {
        this.subcat = subcat;
        this.context = context;
        this.category = category;
    }

    @Override
    public int getItemCount() {
        return subcat.size();
    }

    @Override
    public View getItemView(int position, View view, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.feedbacklayout, container, false);
        TextView name,discription;
        ImageView image;
        RatingBar ratingBar;
        image = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
        discription = view.findViewById(R.id.discription);
        ratingBar = view.findViewById(R.id.ratingBar);

        name.setText(subcat.get(position).getName());
        discription.setText(Html.fromHtml(subcat.get(position).getFeedback()));
        ratingBar.setNumStars(5);
        ratingBar.setRating(Float.parseFloat(subcat.get(position).getStarrate()));

        Glide.with(context).load(imagebaseurl + subcat.get(position).getProfilephoto())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo)
                )
                .into(image);
        return view;

    }
}
