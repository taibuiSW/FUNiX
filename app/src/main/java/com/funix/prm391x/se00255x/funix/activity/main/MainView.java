package com.funix.prm391x.se00255x.funix.activity.main;

import com.funix.prm391x.se00255x.funix.fragment.VideoListFragmentView;

public interface MainView {

    void indicateNetworkStatus(boolean isConnected);

    void registerFragment(VideoListFragmentView fragment);

    void showLogoutDialog();

    void startLoginActivity();

}
