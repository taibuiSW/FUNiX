package com.funix.prm391x.se00255x.funix;

import android.content.Context;
import android.widget.Toast;

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

    private Context mCtx;
    private DatabaseMgr mDbMgr;
    //
    private String mPlayListId;
    private String mNextToken;
    private boolean mEndOfList;
    private int mMaxResults;
    private int mCursor;

    public Fetcher(Context ctx) {
        mCtx = ctx.getApplicationContext();
        mDbMgr = DatabaseMgr.getInstance();
        mDbMgr.clearPlaylist();
    }

    public void initialize() {
        mPlayListId = "UUMOgdURr7d8pOVlc-alkfRg";
        mMaxResults = 20;
    }

    public interface OnFetchCompletedListener {
        void onFetchCompleted();
    }

    public String getLink() {
        return PART_1 + mMaxResults +
                (mNextToken == null ? "" : PART_2 + mNextToken) +
                PART_3 + mPlayListId +
                PART_4 + Const.API_KEY;
    }

    public void getPlaylist(final OnFetchCompletedListener onFetchCompletedListener) {
        if (mEndOfList) return;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(getLink(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mNextToken = response.getString("nextPageToken");
                            mEndOfList = false;
                        } catch (JSONException e) {
                            mNextToken = null;
                            mEndOfList = true;
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

                        if (onFetchCompletedListener != null) {
                            onFetchCompletedListener.onFetchCompleted();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, error.getMessage(), Toast.LENGTH_SHORT).show();
                        if (onFetchCompletedListener != null) {
                            onFetchCompletedListener.onFetchCompleted();
                        }
                    }
                });
        VolleyMgr.getInstance(mCtx).getRequestQueue().add(jsonObjectRequest);
    }
}
