package com.funix.prm391x.se00255x.funix.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.RealtimeAdapter;
import com.funix.prm391x.se00255x.funix.activity.VideoListing.IVideoListingView;
import com.google.firebase.database.Query;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

public class VideoListFragment extends Fragment implements Title, IVideoListFragmentView {
    private RecyclerView mRecyclerView;
    private IVideoListFragmentPresenter mPresenter;
    private RealtimeAdapter mAdapter;

    public static VideoListFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        VideoListFragment fragment = new VideoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mPresenter = new VideoListFragmentPresenter();
        View v = inflater.inflate(R.layout.frag_list, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_list);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((IVideoListingView) getActivity()).getPresenter().registerFragment(this);
    }

    @Override
    public String getTitle() {
        return getArguments().getString(TITLE);
    }

    @Override
    public void addOnScrollListener(OnScrollListener onScrollListener) {
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void bindVideoQuery(Query query) {
        mPresenter.bindVideoQuery(query, mRecyclerView);
    }
}
