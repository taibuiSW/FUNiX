package com.funix.prm391x.se00255x.funix;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funix.prm391x.se00255x.funix.activity.VideoListing.VideoListingActivity;
import com.google.firebase.database.Query;

public class VideoListFragment extends Fragment implements Title, VideoListing {
    private VideoListFragmentViewImpl mVideoListFragmentView;
    private View mRootView;

    public static VideoListFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        VideoListFragment fragment = new VideoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mVideoListFragmentView = new VideoListFragmentViewImpl(inflater, container, this);
        mRootView = mVideoListFragmentView.getRootView();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVideoListFragmentView.CacheLayoutManager(getActivity());
        ((VideoListingActivity) getActivity()).registerFragment(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mVideoListFragmentView.onOrientationChanged(newConfig);
    }

    @Override
    public String getTitle() {
        return getArguments().getString(TITLE);
    }

    @Override
    public void bindVideoQuery(Query query) {
        mVideoListFragmentView.bindVideoQuery(query);
    }
}
