package com.funix.prm391x.se00255x.funix;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

public class RealtimeAdapter extends FirebaseRecyclerAdapter<Video, RealtimeAdapter.VideoViewHolder> {
    private VideoListFragment mVideoListFragment;

    public RealtimeAdapter(VideoListFragment fragment, Query query) {
        super(Video.class, R.layout.item_row, VideoViewHolder.class, query);
        mVideoListFragment = fragment;
    }

    @Override
    protected void populateViewHolder(VideoViewHolder holder, final Video video, int position) {
        holder.populate(mVideoListFragment, video);
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView mThumbnail;
        TextView mTxvTitle;

        public VideoViewHolder(View itemView) {
            super(itemView);
            mThumbnail = (ImageView) itemView.findViewById(R.id.niv_thumb);
            mTxvTitle = (TextView) itemView.findViewById(R.id.txv_title);
        }

        void populate(final VideoListFragment fragment, final Video video) {
            mTxvTitle.setText(video.getTitle());
            GlideApp.with(fragment)
                    .load(video.getThumbnailUrl())
                    .placeholder(R.drawable.place_holder)
                    .into(mThumbnail);
        }
    }
}
