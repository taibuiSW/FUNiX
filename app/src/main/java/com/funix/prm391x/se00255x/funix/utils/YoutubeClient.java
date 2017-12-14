package com.funix.prm391x.se00255x.funix.utils;

import com.funix.prm391x.se00255x.funix.model.retrofit.Playlist;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface YoutubeClient {

    @GET("v3/playlistItems")
    Call<Playlist> getPlaylist(@QueryMap Map<String, String> options);
}
