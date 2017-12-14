package com.funix.prm391x.se00255x.funix.activity.main.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.facebook.login.LoginManager;
import com.funix.prm391x.se00255x.funix.activity.main.view.MainView;
import com.funix.prm391x.se00255x.funix.utils.DatabaseMgr;
import com.funix.prm391x.se00255x.funix.utils.OnScrollVideosLoader;
import com.funix.prm391x.se00255x.funix.videolistfragment.view.VideoListFragment;
import com.funix.prm391x.se00255x.funix.videolistfragment.view.VideoListFragmentView;
import com.google.firebase.auth.FirebaseAuth;

import static android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY;

public class MainPresenterImpl extends BroadcastReceiver implements MainPresenter {
    private MainView mMainView;
    private VideoListFragment mPlaylistFragment;
    private VideoListFragment mHistoryFragment;
    private boolean mFirstLoadSuccess;

    public MainPresenterImpl(MainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = !intent.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false);
        mMainView.indicateNetworkStatus(isConnected);
        if (!mFirstLoadSuccess && isConnected) {
            OnScrollVideosLoader.getInstance().getPlaylist();
            mFirstLoadSuccess = true;
        }
    }

    @Override
    public void bindVideoQuery(VideoListFragmentView fragment) {
        fragment.setUpAdapter(DatabaseMgr.getInstance().getQuery(fragment.getTitle()));
    }

    @Override
    public void logout() {
        // sign out firebase
        FirebaseAuth.getInstance().signOut();

        // logout facebook
        LoginManager.getInstance().logOut();
    }
}
