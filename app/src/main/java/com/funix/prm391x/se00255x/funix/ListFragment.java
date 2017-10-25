package com.funix.prm391x.se00255x.funix;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

public class ListFragment extends Fragment {
    private View mLayout;
    private RecyclerView mRecycler;
    private RealtimeAdapter mAdapter;
    private Query mQuery;
    private OnScrollListener mOnScrollListener;
    private LinearLayoutManager mLinearLayoutMgr;
    private GridLayoutManager mGridLayoutMgr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.frag_list, container, false);
        mRecycler = (RecyclerView) mLayout.findViewById(R.id.recycler_list);

        mLinearLayoutMgr = new LinearLayoutManager(getActivity());
        mGridLayoutMgr = new GridLayoutManager(getActivity(), 2);

        setLayoutMgr(getActivity().getResources().getConfiguration().screenWidthDp);

        mAdapter = new RealtimeAdapter(getContext(), mQuery);
        mRecycler.setAdapter(mAdapter);

        if (mOnScrollListener != null) {
            mRecycler.addOnScrollListener(mOnScrollListener);
        }
        return mLayout;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int current = ((LinearLayoutManager) mRecycler
                .getLayoutManager())
                .findFirstVisibleItemPosition();
        setLayoutMgr(newConfig.screenWidthDp);
        mRecycler.getLayoutManager().scrollToPosition(current);
    }

    private void setLayoutMgr(int screenWidthDp) {
        mRecycler.setLayoutManager(screenWidthDp > 480
                ? mGridLayoutMgr
                : mLinearLayoutMgr);
    }

    public void addOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public void setQuery(Query query) {
        mQuery = query;
    }
}
