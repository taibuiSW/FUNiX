package com.funix.prm391x.se00255x.funix.activity.VideoListing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.funix.prm391x.se00255x.funix.DatabaseMgr;
import com.funix.prm391x.se00255x.funix.Fragment.VideoListFragment;
import com.funix.prm391x.se00255x.funix.OnScrollPreloader;
import com.funix.prm391x.se00255x.funix.R;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

public class VideoListingPresenter implements IVideoListingPresenter {
    private VideoListingActivity mActivity;
    private BroadcastReceiver mReceiver;
    private VideoListFragment mPlaylistFragment;
    private VideoListFragment mHistoryFragment;

    public VideoListingPresenter(VideoListingActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onCreate() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnected = !intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false);
                mActivity.indicateNetworkStatus(isConnected);
            }
        };
        mActivity.registerReceiver(mReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void onDestroy() {
        mActivity.unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void registerFragment(VideoListFragment fragment) {
        fragment.bindVideoQuery(DatabaseMgr.getInstance().getQuery(fragment.getTitle()));
        if (fragment.getTitle().equals(mActivity.getResources().getString(R.string.playlist))) {
            fragment.addOnScrollListener(new OnScrollPreloader(mActivity));
        }
    }
}
