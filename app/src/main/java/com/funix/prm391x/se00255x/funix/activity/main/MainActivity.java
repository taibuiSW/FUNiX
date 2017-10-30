package com.funix.prm391x.se00255x.funix.activity.main;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.funix.prm391x.se00255x.funix.Fetcher;
import com.funix.prm391x.se00255x.funix.OnScrollPreloader;
import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.ViewPagerAdapter;
import com.funix.prm391x.se00255x.funix.fragment.VideoListFragmentView;

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

        Fetcher.getInstance().getPlaylist(this);
        mNetworkIndicator = findViewById(R.id.txv_net_status);

        mPresenter = new MainPresenterImpl(this);
        mPresenter.registerConnectivityReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unregisterConnectivityReceiver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.logout();
        // Todo logout dialog
    }

    @Override
    public void indicateNetworkStatus(boolean isConnected) {
        mNetworkIndicator.setVisibility(isConnected ? GONE : VISIBLE);
    }

    @Override
    public void registerFragment(VideoListFragmentView fragment) {
        if (fragment.getTitle().equals(getResources().getString(R.string.playlist))) {
            fragment.addOnScrollListener(new OnScrollPreloader(this));
        }
        mPresenter.bindVideoQuery(fragment);
    }
}
