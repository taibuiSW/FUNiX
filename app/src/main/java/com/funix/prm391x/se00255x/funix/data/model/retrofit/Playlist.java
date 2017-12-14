
package com.funix.prm391x.se00255x.funix.data.model.retrofit;

import java.util.List;
import com.squareup.moshi.Json;

public class Playlist {

    @Json(name = "nextPageToken")
    private String nextPageToken;
    @Json(name = "items")
    private List<Item> items = null;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
