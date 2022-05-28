package com.eminence.sitasrm.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.eminence.sitasrm.Interface.AddLifecycleCallback;
import com.eminence.sitasrm.Models.VideoModel;
import com.eminence.sitasrm.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    AddLifecycleCallback addLifecycleCallback;
    Context context;
    ArrayList<VideoModel> videolist;

    public VideoAdapter(ArrayList<VideoModel> videolist, Context context, AddLifecycleCallback addLifecycleCallback) {
        this.videolist = videolist;
        this.context = context;
        this.addLifecycleCallback = addLifecycleCallback;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        addLifecycleCallback.addLifeCycleCallBack(holder.youTubePlayerView,holder.mainLayout);

        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                try
                {
                    youTubePlayer.loadVideo(videolist.get(position).getVideo_url(), 0);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videolist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView youTubePlayerView ;
        LinearLayout mainLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            youTubePlayerView = itemView.findViewById(R.id.youTubePlayerView);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}

