package com.funix.prm391x.se00255x.funix.utils;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.funix.prm391x.se00255x.funix.pojo.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VideosFetcher {
    private static final String DEFAULT_BASE_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet";
    private static final String FIELDS = "&fields=items(snippet(resourceId%2FvideoId%2Ctitle))%2CnextPageToken";
    private static final String MAX_RESULTS = "&maxResults=";
    private static final String PAGE_TOKEN = "&pageToken=";
    private static final String PLAYLIST_ID = "&playlistId=";
    private static final String KEY = "&key=";

    private static VideosFetcher mInstance;

    private DatabaseMgr mDbMgr;
    //
    private String mPlayListId = "UUMOgdURr7d8pOVlc-alkfRg";
    private String mNextToken = "";
    private boolean mIsRunning;
    private int mMaxResults = 20;
    private int mCursor;

    public static VideosFetcher getInstance() {
        if (mInstance == null) {
            mInstance = new VideosFetcher();
        }
        return mInstance;
    }

    private VideosFetcher() {
        mDbMgr = DatabaseMgr.getInstance();
        mDbMgr.clearPlaylist();
    }

    // Be careful!! Check mNextToken for null if using requestUrl() outside getPlaylist()
    private String requestUrl() {
        return DEFAULT_BASE_URL + FIELDS +
                MAX_RESULTS + mMaxResults +
                PAGE_TOKEN + mNextToken +
                PLAYLIST_ID + mPlayListId +
                KEY + YoutubePlayer.API_KEY;
    }

    public synchronized void getPlaylist(final Context ctx) {
        if (mNextToken == null || mIsRunning) return;
        mIsRunning = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestUrl(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mNextToken = response.getString("nextPageToken");
                        } catch (JSONException e) {
                            mNextToken = null;
                        }

                        try {
                            JSONArray jArray = new JSONArray(response.getString("items"));
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject item = jArray.getJSONObject(i);
                                JSONObject snippet = item.getJSONObject("snippet");
                                JSONObject resourceId = snippet.getJSONObject("resourceId");
                                mDbMgr.addToPlaylist(mCursor++, new Video(snippet.getString("title"),
                                        resourceId.getString("videoId")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mIsRunning = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        mIsRunning = false;
                    }
                });
        VolleySingleton.getInstance(ctx).getRequestQueue().add(jsonObjectRequest);
    }
}
