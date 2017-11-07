package com.funix.prm391x.se00255x.funix;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fetcher {
    private static final String PART_1 = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=";
    private static final String PART_2 = "&pageToken=";
    private static final String PART_3 = "&playlistId=";
    private static final String PART_4 = "&fields=items(snippet(resourceId%2FvideoId%2Ctitle))%2CnextPageToken&key=";

    private static Fetcher mInstance;

    private DatabaseMgr mDbMgr;
    //
    private String mPlayListId;
    private String mNextToken;
    private boolean mIsRunning;
    private int mMaxResults;
    private int mCursor;

    public static Fetcher getInstance() {
        if (mInstance == null) {
            mInstance = new Fetcher();
        }
        return mInstance;
    }

    private Fetcher() {
        mDbMgr = DatabaseMgr.getInstance();
        mDbMgr.clearPlaylist();
        mPlayListId = "UUMOgdURr7d8pOVlc-alkfRg";
        mNextToken = "";
        mMaxResults = 20;
    }

    // Be careful!! Check PART_2 for null if using getLink() outside getPlaylist()
    private String getLink() {
        return PART_1 + mMaxResults +
                PART_2 + mNextToken +
                PART_3 + mPlayListId +
                PART_4 + Const.API_KEY;
    }

    public synchronized void getPlaylist(final Context ctx) {
        if (mNextToken == null || mIsRunning) return;
        mIsRunning = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(getLink(), null,
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
        VolleyMgr.getInstance(ctx).getRequestQueue().add(jsonObjectRequest);
    }
}
