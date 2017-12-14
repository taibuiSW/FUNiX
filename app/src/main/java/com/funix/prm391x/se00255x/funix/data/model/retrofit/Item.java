
package com.funix.prm391x.se00255x.funix.data.model.retrofit;

import com.squareup.moshi.Json;

public class Item {

    @Json(name = "snippet")
    private Snippet snippet;

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

}
