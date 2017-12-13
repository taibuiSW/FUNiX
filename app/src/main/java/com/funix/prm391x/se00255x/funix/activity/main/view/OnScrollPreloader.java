package com.funix.prm391x.se00255x.funix.activity.main.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.funix.prm391x.se00255x.funix.utils.VideosFetcher;

public class OnScrollPreloader extends RecyclerView.OnScrollListener {
    private static final int VISIBLE_THRESHOLD = 10;

    private Context mCtx;
    private VideosFetcher mFetcher;

    OnScrollPreloader(Context ctx) {
        mCtx = ctx;
        mFetcher = VideosFetcher.getInstance();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        LinearLayoutManager layoutMgr = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutMgr.getItemCount();
        int lastVisibleItem = layoutMgr.findLastVisibleItemPosition();
        if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
            mFetcher.getPlaylist(mCtx);
        }
    }
}
