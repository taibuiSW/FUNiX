package com.funix.prm391x.se00255x.funix;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;

public class FragList extends Fragment {
    private View mLayout;
    private RecyclerView mRecycler;
    private RealtimeAdapter mAdapter;
    private Query mQuery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.frag_list, container, false);
        mRecycler = (RecyclerView) mLayout.findViewById(R.id.recycler_list);
        mAdapter = new RealtimeAdapter(getContext(), mQuery);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mLayout;
    }

    public void setQuery(Query query) {
        mQuery = query;
    }

}
