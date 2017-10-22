package com.funix.prm391x.se00255x.funix;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Context mCtx = this;
    private BroadcastReceiver mReceiver;
    private Fetcher mFetcher;

    private boolean mIsLoading;
    private int visibleThreshold = 10;
    private int mLastVisibleItem;
    private int mTotalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        Log.e("___getMemoryClass", "" + am.getMemoryClass());
//        Log.e("___maxMemory     ", "" + Runtime.getRuntime().maxMemory());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean hasNoConnection = intent.getBooleanExtra(
                        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                if (hasNoConnection) {
                    findViewById(R.id.txv_net_status).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.txv_net_status).setVisibility(View.GONE);
                }
            }
        };
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mFetcher = new Fetcher(mCtx);
        mFetcher.initialize();
        mIsLoading = true;
        mFetcher.getPlaylist(new Fetcher.OnFetchCompletedListener() {
            @Override
            public void onFetchCompleted() {
                mIsLoading = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        FragList playlist = new FragList();
        FragList history = new FragList();
        FragList[] tabs = {playlist, history};
        String[] titles = {"Playlist", "History"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            DatabaseMgr dbMgr = DatabaseMgr.getInstance();
            playlist.setQuery(dbMgr.getPlaylist());
            history.setQuery(dbMgr.getHistory());

            playlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    mTotalItemCount = linearLayoutManager.getItemCount();
                    mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!mIsLoading && mTotalItemCount <= (mLastVisibleItem + visibleThreshold)) {
                        mFetcher.getPlaylist(new Fetcher.OnFetchCompletedListener() {
                            @Override
                            public void onFetchCompleted() {
                                mIsLoading = false;
                            }
                        });
                        mIsLoading = true;
                    }
                }
            });
        }

        @Override
        public Fragment getItem(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
