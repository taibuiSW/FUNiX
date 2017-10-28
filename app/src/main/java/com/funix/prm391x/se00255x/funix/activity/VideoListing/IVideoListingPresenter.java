package com.funix.prm391x.se00255x.funix.activity.VideoListing;

import com.funix.prm391x.se00255x.funix.Fragment.VideoListFragment;

public interface IVideoListingPresenter {
    void onCreate();

    void onDestroy();

    void onBackPressed();

    void registerFragment(VideoListFragment fragment);

}
