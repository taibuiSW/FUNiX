package com.funix.prm391x.se00255x.funix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

public class MainActivity extends AppCompatActivity {
    private VideoListImpl mVideoList;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoList = new VideoListImpl(this);
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
}
