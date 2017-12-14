
package com.funix.prm391x.se00255x.funix.data.model.retrofit;

import com.squareup.moshi.Json;

public class ResourceId {

    @Json(name = "videoId")
    private String videoId;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

}
