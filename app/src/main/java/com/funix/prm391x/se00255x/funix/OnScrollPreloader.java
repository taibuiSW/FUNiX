package com.funix.prm391x.se00255x.funix;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class OnScrollPreloader extends RecyclerView.OnScrollListener {
    private Context mCtx;
    private Fetcher mFetcher;

    public OnScrollPreloader(Context ctx) {
        mCtx = ctx;
        mFetcher = Fetcher.getInstance();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        LinearLayoutManager layoutMgr = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutMgr.getItemCount();
        int lastVisibleItem = layoutMgr.findLastVisibleItemPosition();
        int visibleThreshold = 10;
        if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            mFetcher.getPlaylist(mCtx);
        }
    }
}