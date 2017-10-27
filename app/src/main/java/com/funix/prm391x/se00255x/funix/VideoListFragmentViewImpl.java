package com.funix.prm391x.se00255x.funix;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;

import static android.support.v7.widget.RecyclerView.*;

public class VideoListFragmentViewImpl implements ViewListFragmentView {
    private VideoListFragment mFragment;
    private View mRootView;
    private RecyclerView mRecycler;
    private RealtimeAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutMgr;
    private GridLayoutManager mGridLayoutMgr;

    public VideoListFragmentViewImpl(LayoutInflater inflater, ViewGroup container, VideoListFragment fragment) {
        mFragment = fragment;
        mRootView = inflater.inflate(R.layout.frag_list, container, false);
        mRecycler = (RecyclerView) mRootView.findViewById(R.id.recycler_list);
    }

    public void cacheLayoutManager(Context ctx) {
        mLinearLayoutMgr = new LinearLayoutManager(ctx);
        mGridLayoutMgr = new GridLayoutManager(ctx, 2);
    }

    @Override
    public void addOnScrollListener(OnScrollListener listener) {
        mRecycler.addOnScrollListener(listener);
    }

    @Override
    public void bindVideoQuery(Query query) {
        mAdapter = new RealtimeAdapter(mFragment, query);
        mRecycler.setAdapter(mAdapter);
        setLayoutMgr(mFragment.getResources().getConfiguration().screenWidthDp);
    }

    @Override
    public void onOrientationChanged(Configuration newConfig) {
        switchLayoutMgr(newConfig);
    }

    private void switchLayoutMgr(Configuration newConfig) {
        int current = ((LinearLayoutManager) mRecycler
                .getLayoutManager())
                .findFirstVisibleItemPosition();
        setLayoutMgr(newConfig.screenWidthDp);
        mRecycler.getLayoutManager().scrollToPosition(current);
    }

    private void setLayoutMgr(int screenWidthDp) {
        mRecycler.setLayoutManager(screenWidthDp > 480
                ? mGridLayoutMgr
                : mLinearLayoutMgr);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }
}
