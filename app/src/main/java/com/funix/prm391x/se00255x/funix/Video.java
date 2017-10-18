package com.funix.prm391x.se00255x.funix;

public class Video {
    public String mId;
    public String mTitle;
    public long mTime;

    public Video() {}

    public Video(String title, String id) {
        this.mTitle = title;
        this.mId = id;
    }

    public static String getThumbnailUrl(String id) {
        return "https://img.youtube.com/vi/" + id + "/mqdefault.jpg";
    }
}
