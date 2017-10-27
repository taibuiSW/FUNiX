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

public class VideoListFragment extends Fragment {
    private View mLayout;
    private RecyclerView mRecycler;
    private RealtimeAdapter mAdapter;
    private Query mQuery;
    private LinearLayoutManager mLinearLayoutMgr;
    private GridLayoutManager mGridLayoutMgr;

    public static VideoListFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString("Title", title);
        VideoListFragment fragment = new VideoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.frag_list, container, false);
        mRecycler = (RecyclerView) mLayout.findViewById(R.id.recycler_list);

        mLinearLayoutMgr = new LinearLayoutManager(getActivity());
        mGridLayoutMgr = new GridLayoutManager(getActivity(), 2);

        setLayoutMgr(getActivity().getResources().getConfiguration().screenWidthDp);

        String title = getArguments().getString("Title");

        mAdapter = new RealtimeAdapter(getContext(), DatabaseMgr.getInstance().getQuery(title));
        mRecycler.setAdapter(mAdapter);

        if ("playlist".equals(title)) {
            mRecycler.addOnScrollListener(new OnScrollPreloader(getContext()));
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
}
