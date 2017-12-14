package com.funix.prm391x.se00255x.funix.activity.main.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.activity.login.view.LoginActivity;
import com.funix.prm391x.se00255x.funix.activity.main.presenter.MainPresenterImpl;
import com.funix.prm391x.se00255x.funix.activity.main.viewpageradapter.ViewPagerAdapter;
import com.funix.prm391x.se00255x.funix.utils.OnScrollVideosLoader;
import com.funix.prm391x.se00255x.funix.videolistfragment.view.VideoListFragmentView;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements MainView {
    private MainPresenterImpl mPresenter;
    private View mNetworkIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewpager);
        Resources res = getResources();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                new String[]{res.getString(R.string.playlist), res.getString(R.string.history)});

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mNetworkIndicator = findViewById(R.id.txv_net_status);

        mPresenter = new MainPresenterImpl(this);
        registerReceiver(mPresenter, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mPresenter);
    }

    @Override
    public void onBackPressed() {
        showLogoutDialog();
    }

    @Override
    public void indicateNetworkStatus(boolean isConnected) {
        mNetworkIndicator.setVisibility(isConnected ? GONE : VISIBLE);
    }

    @Override
    public void registerFragment(VideoListFragmentView fragment) {
        if (fragment.getTitle().equals(getResources().getString(R.string.playlist))) {
            fragment.addOnScrollListener(OnScrollVideosLoader.getInstance());
        }
        mPresenter.bindVideoQuery(fragment);
    }

    @Override
    public void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle("Confirm logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Stay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Logout",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.logout();
                                startLoginActivity();
                            }
                        });
        alertBuilder.create().show();
    }

    @Override
    public void startLoginActivity() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
