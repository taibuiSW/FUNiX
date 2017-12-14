package com.funix.prm391x.se00255x.funix.activity.main.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.funix.prm391x.se00255x.funix.data.DatabaseMgr;
import com.funix.prm391x.se00255x.funix.data.model.Video;
import com.funix.prm391x.se00255x.funix.data.model.retrofit.Item;
import com.funix.prm391x.se00255x.funix.data.model.retrofit.Playlist;
import com.funix.prm391x.se00255x.funix.utils.YoutubeClient;
import com.funix.prm391x.se00255x.funix.utils.YoutubeClientCreator;
import com.funix.prm391x.se00255x.funix.utils.YoutubePlayer;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnScrollVideosLoader extends RecyclerView.OnScrollListener
        implements Callback<Playlist> {

    private static final int VISIBLE_THRESHOLD = 10;
    private static final HashMap<String, String> DEFAULT_OPTIONS = new HashMap<>();

    private static OnScrollVideosLoader mInstance;

    private DatabaseMgr mDbMgr;
    private YoutubeClient mYoutubeClient;
    private String mNextToken = "";
    private boolean mIsRunning;
    private int mCursor;

    static {
        DEFAULT_OPTIONS.put("part", "snippet");
        DEFAULT_OPTIONS.put("fields", "items(snippet(resourceId/videoId,title)),nextPageToken");
        DEFAULT_OPTIONS.put("maxResults", "20");
        DEFAULT_OPTIONS.put("playlistId", "UUMOgdURr7d8pOVlc-alkfRg");
        DEFAULT_OPTIONS.put("key", YoutubePlayer.API_KEY);
    }

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
    }

    public synchronized void getPlaylist() {
        if (mNextToken == null || mIsRunning) return;
        mIsRunning = true;
        DEFAULT_OPTIONS.put("pageToken", mNextToken);
        mYoutubeClient.getPlaylist(DEFAULT_OPTIONS).enqueue(this);
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
}
