package com.funix.prm391x.se00255x.funix.activity.VideoListing;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.ViewPagerAdapter;

import static android.view.View.*;

/* This is the view of VideoListingActivity */

public class VideoListingViewImpl implements VideoListingView {
    private View mRootView;
    private ViewPager mViewPager;
    private View mNetworkIndicator;

    public VideoListingViewImpl(Context ctx) {
        mRootView = LayoutInflater.from(ctx).inflate(R.layout.activity_main, (ViewGroup) null);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.viewpager);

        AppCompatActivity activity = (AppCompatActivity) ctx;
        Resources res = ctx.getResources();
        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(),
                new String[]{res.getString(R.string.playlist), res.getString(R.string.history)});
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) mRootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mNetworkIndicator = mRootView.findViewById(R.id.txv_net_status);
    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void indicateNetworkStatus(boolean isConnected) {
        mNetworkIndicator.setVisibility(isConnected ? GONE : VISIBLE);
    }
}
