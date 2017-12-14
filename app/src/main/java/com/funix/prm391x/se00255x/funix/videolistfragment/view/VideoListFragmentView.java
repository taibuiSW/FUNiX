package com.funix.prm391x.se00255x.funix.videolistfragment.view;

import com.funix.prm391x.se00255x.funix.model.Video;
import com.google.firebase.database.Query;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

public interface VideoListFragmentView {
    String TITLE = "TITLE";

    String getTitle();

    void addOnScrollListener(OnScrollListener onScrollListener);

    void setUpAdapter(Query query);

    void onVideoItemClicked(Video video);
}
