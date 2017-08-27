package com.funix.prm391x.se00255x.funix;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "AIzaSyDGMQAc1eMvQhf24_EwRS6UvoDccA6Fk-w";
    private static final String REQUEST = "https://goo.gl/L9V31Y";

    private Context mCtx = this;
    private ArrayList<Video> mPlayList = new ArrayList<>();
    private ListView mLsvVideo;
    private CustomAdapter mAdapter;
    private View mHeader;
    private DatabaseMgr mDatabase;
    private BroadcastReceiver mReceiver;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("xDays - xTalks");

        mDatabase = new DatabaseMgr(this, getIntent().getExtras().getString("username"));
        mLsvVideo = (ListView) findViewById(R.id.list_view);
        mAdapter = new CustomAdapter(mCtx, R.layout.list_row, R.id.txv_title, mPlayList);
        mLsvVideo.setAdapter(mAdapter);
        mLsvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video = mPlayList.get(position);
                mDatabase.modifyHistory(video);
                Intent intent;
                if (isYouTubeAppUsable()) {
                    intent = YouTubeStandalonePlayer
                            .createVideoIntent((Activity) mCtx, API_KEY, video.getId());
                } else {
                    intent = new Intent(mCtx, WebViewActivity.class);
                    intent.putExtra("id", video.getId());
                }
                startActivity(intent);
            }
        });

        mHeader = getLayoutInflater().inflate(R.layout.header_net_status, null);
        mHeader.setOverScrollMode(View.OVER_SCROLL_IF_CONTENT_SCROLLS);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int headerCount = mLsvVideo.getHeaderViewsCount();
                boolean isConnected = hasInternetAccess();
                // these conditions might look stupid but they're necessary
                if (!isConnected && headerCount == 0) {
                    mLsvVideo.addHeaderView(mHeader);
                } else if (isConnected && headerCount > 0) {
                    mLsvVideo.removeHeaderView(mHeader);
                }
            }
        };
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        getPlaylist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.playlist_toggle:
                if (getTitle().equals("History")) {
                    //switch from 'History' to 'xDays - xTalks'
                    setTitle("xDays - xTalks");
                    item.setIcon(R.drawable.ic_history_24dp);
                    getPlaylist();
                } else {
                    //switch from 'xDays - xTalks' to 'History'
                    setTitle("History");
                    item.setIcon(R.drawable.ic_view_list_24dp);
                    getHistory();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
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
                                startActivity(new Intent(mCtx, LoginActivity.class));
                            }
                        });
        alertBuilder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private boolean hasInternetAccess() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void getHistory() {
        mPlayList.clear();
        mPlayList.addAll(mDatabase.getHistory());
        mAdapter.notifyDataSetChanged();
    }

    private void getPlaylist() {
        final ProgressDialog progressDialog = ProgressDialog.show(mCtx,
                "Please wait...", "Checking playlist...");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(REQUEST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mPlayList.clear();
                        try {
                            JSONArray jArray = new JSONArray(response.getString("items"));
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject item = jArray.getJSONObject(i);
                                JSONObject snippet = item.getJSONObject("snippet");
                                JSONObject resourceId = snippet.getJSONObject("resourceId");
                                mPlayList.add(new Video(snippet.getString("title"), resourceId.getString("videoId")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, "Unfortunately something went wrong :(", Toast.LENGTH_SHORT).show();
                    }
                });
        NetworkMgr.getInstance(mCtx).getRequestQueue().add(jsonObjectRequest);
    }

    boolean isYouTubeAppUsable() {
        try {
            return getPackageManager().getApplicationInfo("com.google.android.youtube", 0).enabled
                    && YouTubeIntents.getInstalledYouTubeVersionCode(mCtx) >= 4216;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static class ViewHolder {
        NetworkImageView nivThumb;
        TextView txvTitle;
    }

    private class CustomAdapter extends ArrayAdapter<Video> {
        private LayoutInflater layoutInflater;

        CustomAdapter(@NonNull Context context, @LayoutRes int resource,
                      @IdRes int textViewResourceId, @NonNull List<Video> objects) {
            super(context, resource, textViewResourceId, objects);
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_row, parent, false);
                holder = new ViewHolder();
                holder.nivThumb = (NetworkImageView) convertView.findViewById(R.id.niv_thumb);
                holder.txvTitle = (TextView) convertView.findViewById(R.id.txv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Video video = mPlayList.get(position);
            holder.nivThumb.setImageUrl(Video.getThumbnailUrl(video.getId()),
                    NetworkMgr.getInstance(mCtx).getImageLoader());
            holder.txvTitle.setText(video.getTitle());
            return convertView;
        }
    }
}
