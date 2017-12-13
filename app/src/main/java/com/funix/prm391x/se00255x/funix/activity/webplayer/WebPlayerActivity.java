package com.funix.prm391x.se00255x.funix.activity.webplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.webkit.WebView;

import com.funix.prm391x.se00255x.funix.R;

public class WebPlayerActivity extends Activity {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_web_player);

        String videoId = getIntent().getStringExtra("id");
        WebView wbvVideo = (WebView) findViewById(R.id.wbv_video);
        wbvVideo.getSettings().setJavaScriptEnabled(true);
        wbvVideo.loadData("<html><body style='margin:0;padding:0;'><iframe width=100% height=100% src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" </iframe></body></html>", "text/html", "UTF-8");
    }
}
