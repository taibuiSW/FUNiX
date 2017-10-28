package com.funix.prm391x.se00255x.funix.Fragment;

import com.google.firebase.database.Query;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

public interface IVideoListFragmentView {

    void addOnScrollListener(OnScrollListener onScrollListener);

    void bindVideoQuery(Query query);

}
