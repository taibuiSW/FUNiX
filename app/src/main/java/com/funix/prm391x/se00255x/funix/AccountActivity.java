package com.funix.prm391x.se00255x.funix;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountActivity extends AppCompatActivity {
    private Context mCtx = this;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean hasNoConnection = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                Log.e("___hasNoConnection", "" + hasNoConnection);
                if (hasNoConnection) {
                    findViewById(R.id.txv_net_status).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.txv_net_status).setVisibility(View.GONE);
                    getPlaylist();
                }
            }
        };
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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

    private static final String REQUEST_URL = "https://goo.gl/L9V31Y";
    private void getPlaylist() {
        final ProgressDialog progressDialog = ProgressDialog.show(mCtx,
                "Please wait...", "Checking playlist...");
        final DatabaseMgr dbMgr = DatabaseMgr.getInstance();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(REQUEST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jArray = new JSONArray(response.getString("items"));
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject item = jArray.getJSONObject(i);
                                JSONObject snippet = item.getJSONObject("snippet");
                                JSONObject resourceId = snippet.getJSONObject("resourceId");
                                dbMgr.addToPlaylist(i, new Video(snippet.getString("title"),
                                        resourceId.getString("videoId")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyMgr.getInstance(mCtx).getRequestQueue().add(jsonObjectRequest);
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
