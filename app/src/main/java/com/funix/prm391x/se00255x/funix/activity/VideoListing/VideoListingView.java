package com.funix.prm391x.se00255x.funix.activity.VideoListing;

import android.support.v4.view.ViewPager;

import com.funix.prm391x.se00255x.funix.ViewMvp;


public interface VideoListingView extends ViewMvp {
    ViewPager getViewPager();

    void indicateNetworkStatus(boolean isConnected);
}
