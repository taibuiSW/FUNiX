package com.funix.prm391x.se00255x.funix.activity.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.funix.prm391x.se00255x.funix.DatabaseMgr;
import com.funix.prm391x.se00255x.funix.fragment.VideoListFragment;
import com.funix.prm391x.se00255x.funix.fragment.VideoListFragmentView;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

public class MainPresenterImpl implements MainPresenter {
    private MainView mMainView;
    private BroadcastReceiver mReceiver;
    private VideoListFragment mPlaylistFragment;
    private VideoListFragment mHistoryFragment;

    public MainPresenterImpl(MainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void registerConnectivityReceiver() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnected = !intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false);
                mMainView.indicateNetworkStatus(isConnected);
            }
        };
        ((Activity) mMainView).registerReceiver(mReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void unregisterConnectivityReceiver() {
        ((Activity) mMainView).unregisterReceiver(mReceiver);
    }

    @Override
    public void logout() {

    }

    @Override
    public void bindVideoQuery(VideoListFragmentView fragment) {
        fragment.setUpAdapter(DatabaseMgr.getInstance()
                .getQuery(fragment.getTitle()));
    }
}
