package com.funix.prm391x.se00255x.funix.activity.VideoListing;

public interface IVideoListingView {

    void indicateNetworkStatus(boolean isConnected);

    IVideoListingPresenter getPresenter();

}
