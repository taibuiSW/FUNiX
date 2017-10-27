package com.funix.prm391x.se00255x.funix.activity.VideoListing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.funix.prm391x.se00255x.funix.DatabaseMgr;
import com.funix.prm391x.se00255x.funix.Fetcher;
import com.funix.prm391x.se00255x.funix.OnScrollPreloader;
import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.VideoListFragment;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

public class VideoListingActivity extends AppCompatActivity {
    private VideoListingViewImpl mVideoList;
    private BroadcastReceiver mReceiver;
    private VideoListFragment mPlaylistFragment;
    private VideoListFragment mHistoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoList = new VideoListingViewImpl(this);
        setContentView(mVideoList.getRootView());

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnected = !intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false);
                mVideoList.indicateNetworkStatus(isConnected);
            }
        };

        registerReceiver(mReceiver, new IntentFilter(CONNECTIVITY_ACTION));

        Fetcher.getInstance().getPlaylist(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Todo logout dialog
    }

    public void registerFragment(VideoListFragment fragment) {
        fragment.bindVideoQuery(DatabaseMgr.getInstance().getQuery(fragment.getTitle()));
        if (fragment.getTitle().equals(getResources().getString(R.string.playlist))) {
            fragment.addOnScrollListener(new OnScrollPreloader(this));
        }
    }
}
