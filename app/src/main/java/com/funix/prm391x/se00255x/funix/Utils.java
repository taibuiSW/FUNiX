package com.funix.prm391x.se00255x.funix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class Utils {
    public static boolean hasInternetAccess(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isYouTubeAppUsable(Context ctx) {
        return YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(ctx)
                .equals(YouTubeInitializationResult.SUCCESS);
    }

    public static void startYoutubePlayer(Context ctx, Video video) {
        Intent intent;
        if (isYouTubeAppUsable(ctx)) {
            intent = YouTubeStandalonePlayer
                    .createVideoIntent((Activity) ctx, Const.API_KEY, video.mId);
        } else {
            intent = new Intent(ctx, WebViewActivity.class);
            intent.putExtra("id", video.mId);
        }
        ctx.startActivity(intent);
    }
}
