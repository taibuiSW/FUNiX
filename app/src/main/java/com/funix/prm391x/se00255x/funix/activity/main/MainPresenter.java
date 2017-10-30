package com.funix.prm391x.se00255x.funix.activity.main;

import com.funix.prm391x.se00255x.funix.fragment.VideoListFragmentView;

public interface MainPresenter {
    void registerConnectivityReceiver();

    void unregisterConnectivityReceiver();

    void logout();

    void bindVideoQuery(VideoListFragmentView fragment);

}
