package com.funix.prm391x.se00255x.funix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RealtimeAdapter extends FirebaseRecyclerAdapter<Video, RealtimeAdapter.VideoViewHolder> {
    private Context mCtx;

    public RealtimeAdapter(Context ctx, FirebaseRecyclerOptions<Video> options) {
        super(options);
        mCtx = ctx;
    }

    @Override
    protected void onBindViewHolder(VideoViewHolder holder, int position, Video video) {
        holder.mTxvTitle.setText(video.getTitle());
        GlideApp.with(mCtx)
                .load(Video.getThumbnailUrl(video))
                .placeholder(R.drawable.place_holder)
                .into(holder.mThumbnail);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new VideoViewHolder(itemView);
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView mThumbnail;
        TextView mTxvTitle;
        Context mCtx;

        public VideoViewHolder(View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.niv_thumb);
            mTxvTitle = itemView.findViewById(R.id.txv_title);
        }
    }
}
