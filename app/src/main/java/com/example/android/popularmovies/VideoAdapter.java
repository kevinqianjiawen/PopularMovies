package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin on 8/18/2016.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.videoViewHolder> {

    private List<AndroidFlavor.video> videoList;

    public VideoAdapter(List<AndroidFlavor.video> videoList) {
        this.videoList = videoList;
    }

    @Override
    public int getItemCount() {
        return videoList.size()-1;
    }

    @Override
    public void onBindViewHolder(final videoViewHolder videoViewHolder, int i) {
        final String previewKey = videoList.get(i+1).getKey();
        final String urlPreview = "http://img.youtube.com/vi/"+ previewKey + "/0.jpg";
        Picasso.with(videoViewHolder.preview.getContext()).load(urlPreview).into(videoViewHolder.preview);

        videoViewHolder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + previewKey));
                if (intent.resolveActivity(videoViewHolder.preview.getContext().getPackageManager()) != null) {
                    videoViewHolder.preview.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public videoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_item_video, viewGroup, false);

        return new videoViewHolder(itemView);
    }

    public static class videoViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected ImageView preview;

        public videoViewHolder(View v) {
            super(v);
            view = v;
            preview = (ImageView) v.findViewById(R.id.preview_item);
        }
    }
}
