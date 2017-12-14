package com.funix.prm391x.se00255x.funix.videolistfragment.presenter;

import com.funix.prm391x.se00255x.funix.data.DatabaseMgr;
import com.funix.prm391x.se00255x.funix.data.model.Video;

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
