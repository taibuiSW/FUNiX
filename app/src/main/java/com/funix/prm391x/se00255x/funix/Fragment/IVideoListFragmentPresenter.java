package com.funix.prm391x.se00255x.funix.Fragment;

import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.Query;

public interface IVideoListFragmentPresenter {

    void bindVideoQuery(Query query, RecyclerView recyclerView);

}
