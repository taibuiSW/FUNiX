package com.funix.prm391x.se00255x.funix;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import static android.support.v7.widget.RecyclerView.*;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    FragList playlist = new FragList();
    FragList history = new FragList();
    FragList[] tabs = {playlist, history};
    String[] titles = {"Playlist", "History"};
    private boolean mIsLoading;
    private Fetcher mFetcher;
    private Context mCtx;

    public ViewPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mCtx = ctx;

        DatabaseMgr dbMgr = DatabaseMgr.getInstance();
        playlist.setQuery(dbMgr.getPlaylist());
        history.setQuery(dbMgr.getHistory());

        playlist.addOnScrollListener(getOnScrollPreloader());

        mFetcher = new Fetcher(mCtx);
        mFetcher.initialize();
        mIsLoading = true;
        mFetcher.getPlaylist(new Fetcher.OnFetchCompletedListener() {
            @Override
            public void onFetchCompleted() {
                mIsLoading = false;
            }
        });
    }

    private OnScrollListener getOnScrollPreloader() {
        return new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                preload(recyclerView);
            }
        };
    }

    private void preload(RecyclerView recyclerView) {
        LayoutManager layoutMgr = recyclerView.getLayoutManager();
        int totalItemCount = layoutMgr.getItemCount();
        int lastVisibleItem = 0;
        if (layoutMgr instanceof LinearLayoutManager) {
            lastVisibleItem = ((LinearLayoutManager) layoutMgr).findLastVisibleItemPosition();
        } else {
            int[] lastVisibleItems = ((StaggeredGridLayoutManager) layoutMgr).findLastVisibleItemPositions(null);
            for (int i : lastVisibleItems) {
                if (i > lastVisibleItem) {
                    lastVisibleItem = i;
                }
            }
        }
        int visibleThreshold = 10;
        if (!mIsLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            mFetcher.getPlaylist(new Fetcher.OnFetchCompletedListener() {
                @Override
                public void onFetchCompleted() {
                    mIsLoading = false;
                }
            });
            mIsLoading = true;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
