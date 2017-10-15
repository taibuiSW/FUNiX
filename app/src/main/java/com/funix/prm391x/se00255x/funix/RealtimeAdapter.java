package com.funix.prm391x.se00255x.funix;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

public class RealtimeAdapter extends FirebaseRecyclerAdapter<Video, RealtimeAdapter.VHolder> {
    private Context mCtx;
    private DatabaseMgr dbMgr;

    public RealtimeAdapter(Context ctx, Query query) {
        super(Video.class, R.layout.list_row, VHolder.class, query);
        mCtx = ctx;
        dbMgr = DatabaseMgr.getInstance();
    }

    @Override
    protected void populateViewHolder(VHolder holder, final Video video, int position) {
        holder.mThumbnail.setImageUrl(Video.getThumbnailUrl(video.mId),
                VolleyMgr.getInstance(mCtx).getImageLoader());
        holder.mTxvTitle.setText(video.mTitle);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMgr.modifyHistory(video);
                Utils.startYoutubePlayer(mCtx, video);
            }
        });
    }

    public static class VHolder extends RecyclerView.ViewHolder {
        NetworkImageView mThumbnail;
        TextView mTxvTitle;

        public VHolder(View itemView) {
            super(itemView);
            mThumbnail = (NetworkImageView) itemView.findViewById(R.id.niv_thumb);
            mTxvTitle = (TextView) itemView.findViewById(R.id.txv_title);
        }
    }
}
