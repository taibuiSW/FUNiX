package com.funix.prm391x.se00255x.funix;

class Video {
    private String mTitle;
    private String mId;

    Video(String title, String id) {
        this.mTitle = title;
        this.mId = id;
    }

    static String getThumbnailUrl(String id) {
        return "https://i.ytimg.com/vi/" + id + "/mqdefault.jpg";
    }

    String getTitle() {
        return mTitle;
    }

    String getId() {
        return mId;
    }
}
