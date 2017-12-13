package com.funix.prm391x.se00255x.funix.videolistfragment.firebaseadapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.funix.prm391x.se00255x.funix.utils.GlideApp;
import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.pojo.Video;
import com.funix.prm391x.se00255x.funix.videolistfragment.view.VideoListFragmentView;

public class RealtimeAdapter extends FirebaseRecyclerAdapter<Video, RealtimeAdapter.VideoViewHolder> {
    private VideoListFragmentView mFragment;

    public RealtimeAdapter(VideoListFragmentView fragment, FirebaseRecyclerOptions<Video> options) {
        super(options);
        mFragment = fragment;
    }

    @Override
    protected void onBindViewHolder(VideoViewHolder holder, int position, final Video video) {
        holder.mTxvTitle.setText(video.getTitle());
        GlideApp.with((Fragment) mFragment)
                .load(Video.getThumbnailUrl(video))
                .placeholder(R.drawable.place_holder)
                .into(holder.mThumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.onVideoItemClicked(video);
            }
        });
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new VideoViewHolder(itemView);
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView mThumbnail;
        TextView mTxvTitle;

        VideoViewHolder(View itemView) {
            super(itemView);
            mThumbnail = itemView.findViewById(R.id.niv_thumb);
            mTxvTitle = itemView.findViewById(R.id.txv_title);
        }
    }
}
