package com.funix.prm391x.se00255x.funix.videolistfragment.view;

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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.videolistfragment.firebaseadapter.RealtimeAdapter;
import com.funix.prm391x.se00255x.funix.utils.YoutubePlayer;
import com.funix.prm391x.se00255x.funix.data.model.Video;
import com.funix.prm391x.se00255x.funix.activity.main.view.MainView;
import com.funix.prm391x.se00255x.funix.videolistfragment.presenter.VideoListPresenter;
import com.funix.prm391x.se00255x.funix.videolistfragment.presenter.VideoListPresenterImpl;
import com.google.firebase.database.Query;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

public class VideoListFragment extends Fragment implements VideoListFragmentView {
    private RecyclerView mRecyclerView;
    private VideoListPresenter mPresenter;
    private RealtimeAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutMgr;
    private GridLayoutManager mGridLayoutMgr;

    public static VideoListFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(VideoListFragmentView.TITLE, title);
        VideoListFragment fragment = new VideoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mPresenter = new VideoListPresenterImpl();
        View v = inflater.inflate(R.layout.frag_list, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_list);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mLinearLayoutMgr = new LinearLayoutManager(getContext());
        mGridLayoutMgr = new GridLayoutManager(getContext(), 2);
        setLayoutMgr(getResources().getConfiguration().screenWidthDp);
        ((MainView) getActivity()).registerFragment(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.stopListening();
    }

    @Override
    public String getTitle() {
        return getArguments().getString(VideoListFragmentView.TITLE);
    }

    @Override
    public void addOnScrollListener(OnScrollListener onScrollListener) {
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void setUpAdapter(Query query) {
        FirebaseRecyclerOptions<Video> options = new FirebaseRecyclerOptions
                .Builder<Video>()
                .setQuery(query, Video.class)
                .build();
        mAdapter = new RealtimeAdapter(this, options);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    public void onVideoItemClicked(Video video) {
        mPresenter.updateWatchHistory(video);
        YoutubePlayer.start(this.getContext(), video);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switchLayoutMgr(newConfig);
    }

    private void switchLayoutMgr(Configuration newConfig) {
        int current = ((LinearLayoutManager) mRecyclerView
                .getLayoutManager())
                .findFirstVisibleItemPosition();
        setLayoutMgr(newConfig.screenWidthDp);
        mRecyclerView.getLayoutManager().scrollToPosition(current);
    }

    private void setLayoutMgr(int screenWidthDp) {
        mRecyclerView.setLayoutManager(screenWidthDp > 480
                ? mGridLayoutMgr
                : mLinearLayoutMgr);
    }
}
