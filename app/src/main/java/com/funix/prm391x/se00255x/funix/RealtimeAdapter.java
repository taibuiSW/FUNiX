package com.funix.prm391x.se00255x.funix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

public class RealtimeAdapter extends FirebaseRecyclerAdapter<Video, RealtimeAdapter.VideoViewHolder> {
    private Context mCtx;
    private DatabaseMgr dbMgr;

    public RealtimeAdapter(Context ctx, Query query) {
        super(Video.class, R.layout.item_row, VideoViewHolder.class, query);
        mCtx = ctx;
        dbMgr = DatabaseMgr.getInstance();
    }

    @Override
    protected void populateViewHolder(VideoViewHolder holder, final Video video, int position) {
        VideoViewHolder vHolder = (VideoViewHolder) holder;
        vHolder.mThumbnail.setImageUrl(Video.getThumbnailUrl(video.mId),
                VolleyMgr.getInstance(mCtx).getImageLoader());
        vHolder.mTxvTitle.setText(video.mTitle);
        vHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMgr.modifyHistory(video);
                Utils.startYoutubePlayer(mCtx, video);
            }
        });
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView mThumbnail;
        TextView mTxvTitle;

        public VideoViewHolder(View itemView) {
            super(itemView);
            mThumbnail = (NetworkImageView) itemView.findViewById(R.id.niv_thumb);
            mTxvTitle = (TextView) itemView.findViewById(R.id.txv_title);
        }
    }
}
