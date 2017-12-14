package com.funix.prm391x.se00255x.funix.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.funix.prm391x.se00255x.funix.model.Video;
import com.funix.prm391x.se00255x.funix.model.retrofit.Item;
import com.funix.prm391x.se00255x.funix.model.retrofit.Playlist;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnScrollVideosLoader extends RecyclerView.OnScrollListener
        implements Callback<Playlist> {

    private static final int VISIBLE_THRESHOLD = 10;

    private static OnScrollVideosLoader mInstance;

    private DatabaseMgr mDbMgr;
    private YoutubeClient mYoutubeClient;
    private HashMap<String, String> mOptions;
    private String mNextToken = "";
    private boolean mIsRunning;
    private int mCursor;

    public static OnScrollVideosLoader getInstance() {
        if (mInstance == null) {
            mInstance = new OnScrollVideosLoader();
        }
        return mInstance;
    }

    private OnScrollVideosLoader() {
        mDbMgr = DatabaseMgr.getInstance();
        mDbMgr.clearPlaylist();
        mYoutubeClient = YoutubeClientCreator.create();
        setDefaultOptions();
    }

    public synchronized void getPlaylist() {
        if (mNextToken == null || mIsRunning) return;
        mIsRunning = true;
        mOptions.put("pageToken", mNextToken);
        mYoutubeClient.getPlaylist(mOptions).enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
        Playlist playlist = response.body();
        if (playlist == null) return;
        mNextToken = playlist.getNextPageToken();
        List<Item> items = playlist.getItems();
        for (Item item : items) {
            mDbMgr.addToPlaylist(mCursor++, new Video(item.getSnippet().getTitle(),
                    item.getSnippet().getResourceId().getVideoId()));
        }
        mIsRunning = false;
    }

    @Override
    public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable t) {
        mIsRunning = false;
        t.printStackTrace();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        LinearLayoutManager layoutMgr = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutMgr.getItemCount();
        int lastVisibleItem = layoutMgr.findLastVisibleItemPosition();
        if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
            getPlaylist();
        }
    }

    private void setDefaultOptions() {
        mOptions = new HashMap<>();
        mOptions.put("part", "snippet");
        mOptions.put("fields", "items(snippet(resourceId/videoId,title)),nextPageToken");
        mOptions.put("maxResults", "20");
        mOptions.put("playlistId", "UUMOgdURr7d8pOVlc-alkfRg");
        mOptions.put("key", YoutubePlayer.API_KEY);
    }
}
