package com.funix.prm391x.se00255x.funix.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.funix.prm391x.se00255x.funix.model.Video;
import com.funix.prm391x.se00255x.funix.activity.webplayer.WebPlayerActivity;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class YoutubePlayer {
    public static final String API_KEY = "AIzaSyDGMQAc1eMvQhf24_EwRS6UvoDccA6Fk-w";

    private static boolean isYouTubeAppUsable(Context ctx) {
        return YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(ctx)
                .equals(YouTubeInitializationResult.SUCCESS);
    }

    public static void start(Context ctx, Video video) {
        Intent intent;
        if (isYouTubeAppUsable(ctx)) {
            intent = YouTubeStandalonePlayer
                    .createVideoIntent((Activity) ctx, API_KEY, video.getId());
        } else {
            intent = new Intent(ctx, WebPlayerActivity.class);
            intent.putExtra("id", video.getId());
        }
        ctx.startActivity(intent);
    }
}
