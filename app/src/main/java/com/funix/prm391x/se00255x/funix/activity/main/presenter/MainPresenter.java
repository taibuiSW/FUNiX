package com.funix.prm391x.se00255x.funix.activity.main.presenter;

import com.funix.prm391x.se00255x.funix.videolistfragment.view.VideoListFragmentView;

public interface MainPresenter {

    void bindVideoQuery(VideoListFragmentView fragment);

    void logout();

}
