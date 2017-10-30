package com.funix.prm391x.se00255x.funix.fragment.presenter;

import com.funix.prm391x.se00255x.funix.DatabaseMgr;
import com.funix.prm391x.se00255x.funix.Video;

public class VideoListPresenterImpl implements VideoListPresenter {
    private DatabaseMgr mDbMgr;

    public VideoListPresenterImpl() {
        mDbMgr = DatabaseMgr.getInstance();
    }

    @Override
    public void updateWatchHistory(Video video) {
        mDbMgr.updateHistory(video);
    }
}
