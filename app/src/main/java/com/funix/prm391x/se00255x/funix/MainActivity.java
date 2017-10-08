package com.funix.prm391x.se00255x.funix;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "AIzaSyDGMQAc1eMvQhf24_EwRS6UvoDccA6Fk-w";
    private static final String REQUEST_URL = "https://goo.gl/L9V31Y";

    private Context mCtx = this;
    private LayoutInflater mInflater;
    private ArrayList<Video> mPlaylist = new ArrayList<>();
    private ArrayList<Video> mPlaylistHolder = new ArrayList<>();
    private ListView mLsvVideo;
    private CustomAdapter mAdapter;
    private View mHeader;
    private DatabaseMgr mDatabase;
    private BroadcastReceiver mReceiver;
    private MenuItem mToggle;
    private Bundle mSavedState;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSavedState = savedInstanceState;
        Log.e("___", "onCreate");

        setContentView(R.layout.activity_main);
        setTitle("xDays - xTalks");

        // get layout inflater for ListView and header
        mInflater = LayoutInflater.from(mCtx);

        // get mDatabase
        mDatabase = DatabaseMgr.getInstance();

        // initializing ListView
        mLsvVideo = (ListView) findViewById(R.id.list_view);
        mAdapter = new CustomAdapter();

        mLsvVideo.setAdapter(mAdapter);
        mLsvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!hasInternetAccess()) {
                    position -= 1;
                }
                Video video = mPlaylist.get(position);
                mDatabase.modifyHistory(video);
                Intent intent;
                if (isYouTubeAppUsable()) {
                    intent = YouTubeStandalonePlayer
                            .createVideoIntent((Activity) mCtx, API_KEY, video.mId);
                } else {
                    intent = new Intent(mCtx, WebViewActivity.class);
                    intent.putExtra("id", video.mId);
                }
                startActivity(intent);
            }
        });

        // create header (for network status)
        mHeader = mInflater.inflate(R.layout.header_net_status, null);
        mLsvVideo.addHeaderView(mHeader);

        // receiver for detecting changes in network state
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

                    /* by clearing mPlaylist & mPlaylistHolder here, refreshPlaylist()
                     * will update playlist as soon as re-connect to the internet
                     */
                    mPlaylistHolder.clear();

                    // mToggle will be null when onCreate is called
                    if (mToggle == null) {
                        getPlaylist();
                    } else {
                        switchToPlaylist();
                    }
                }
            }
        };
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_in_main, menu);
        mToggle = menu.findItem(R.id.playlist_toggle);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.playlist_toggle) {
            if (getTitle().equals("History")) {
                switchToPlaylist();
            } else {
                switchToHistory();
            }
        } else {
            for (Video v : mPlaylist) {
                Log.e("___" + v.mTime, v.mTitle);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // switching ListView form History mode to xTalks mode
    private void switchToPlaylist() {
        setTitle("xDays - xTalks");
        mToggle.setIcon(R.drawable.ic_history_24dp);
        refreshPlaylist();
    }

    // switching ListView form xTalks mode to History mode
    private void switchToHistory() {
        setTitle("History");
        mToggle.setIcon(R.drawable.ic_view_list_24dp);
        getHistory();
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
                                startActivity(new Intent(mCtx, AccountActivity.class));
                            }
                        });
        alertBuilder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("___onDestroy", "destroyed");
        unregisterReceiver(mReceiver);
    }

    private boolean hasInternetAccess() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void getHistory() {
        // backup xTalks playlist
        if (!mPlaylist.isEmpty()) {
            mPlaylistHolder.clear();
            mPlaylistHolder.addAll(mPlaylist);
        }
        // replace by history
        mAdapter.clear();
        mDatabase.getHistory(mAdapter);
//        mAdapter.addAll();
    }

    private void refreshPlaylist() {
        if (mPlaylistHolder.isEmpty() && hasInternetAccess()) {
            getPlaylist();
        } else {
            // restore mPlaylist
            mAdapter.clear();
            mAdapter.addAll(mPlaylistHolder);
        }
    }

    private void getPlaylist() {
        final ProgressDialog progressDialog = ProgressDialog.show(mCtx,
                "Please wait...", "Checking playlist...");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(REQUEST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mPlaylist.clear();
                        try {
                            JSONArray jArray = new JSONArray(response.getString("items"));
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject item = jArray.getJSONObject(i);
                                JSONObject snippet = item.getJSONObject("snippet");
                                JSONObject resourceId = snippet.getJSONObject("resourceId");
                                mPlaylist.add(new Video(snippet.getString("title"),
                                        resourceId.getString("videoId")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (mSavedState != null
                                && mSavedState.containsKey("state")) {
                            Log.e("___ok", "ok");
                            mLsvVideo.onRestoreInstanceState(mSavedState
                                    .getParcelable("state"));
                        }

                        mAdapter.notifyDataSetChanged();
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
        NetworkMgr.getInstance(mCtx).getRequestQueue().add(jsonObjectRequest);
    }

    boolean isYouTubeAppUsable() {
        return YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(mCtx)
                .equals(YouTubeInitializationResult.SUCCESS);
    }

    private static class ViewHolder {
        NetworkImageView mThumbnail;
        TextView mTxvTitle;
    }

    class CustomAdapter extends ArrayAdapter<Video> {
        CustomAdapter() {
            super(mCtx, R.layout.list_row, mPlaylist);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_row, parent, false);
                holder = new ViewHolder();
                holder.mThumbnail = (NetworkImageView) convertView.findViewById(R.id.niv_thumb);
                holder.mTxvTitle = (TextView) convertView.findViewById(R.id.txv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Video video = mPlaylist.get(position);
            holder.mThumbnail.setImageUrl(Video.getThumbnailUrl(video.mId),
                    NetworkMgr.getInstance(mCtx).getImageLoader());
            holder.mTxvTitle.setText(video.mTitle);
            return convertView;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("___save state", "saving state");
        outState.putParcelable("state", mLsvVideo.onSaveInstanceState());
    }
}