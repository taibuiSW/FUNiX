package com.funix.prm391x.se00255x.funix.activity.VideoListing;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.ViewPagerAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class VideoListingActivity extends AppCompatActivity implements IVideoListingView {
    private VideoListingPresenter mPresenter;
    private ViewPager mViewPager;
    private View mNetworkIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new VideoListingPresenter(this);

        mViewPager = findViewById(R.id.viewpager);
        Resources res = getResources();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                new String[]{res.getString(R.string.playlist), res.getString(R.string.history)});

        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mNetworkIndicator = findViewById(R.id.txv_net_status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.onBackPressed();
        // Todo logout dialog
    }

    @Override
    public void indicateNetworkStatus(boolean isConnected) {
        mNetworkIndicator.setVisibility(isConnected ? GONE : VISIBLE);
    }

    @Override
    public IVideoListingPresenter getPresenter() {
        return mPresenter;
    }
}
