package com.funix.prm391x.se00255x.funix;

public class Video {
    private static final String THUMB_URL = "https://img.youtube.com/vi/%s/maxresdefault.jpg";

    private String mId;
    private String mTitle;
    private long mTime;

    public Video() {}

    public Video(String title, String id) {
        mTitle = title;
        mId = id;
    }

    public String getThumbnailUrl() {
        return String.format(THUMB_URL, mId);
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getTime() {
        return mTime;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setTime(long time) {
        mTime = time;
    }
}
