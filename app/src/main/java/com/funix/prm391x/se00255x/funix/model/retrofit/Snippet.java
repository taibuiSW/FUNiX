
package com.funix.prm391x.se00255x.funix.model.retrofit;

import com.squareup.moshi.Json;

public class Snippet {

    @Json(name = "title")
    private String title;
    @Json(name = "resourceId")
    private ResourceId resourceId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ResourceId getResourceId() {
        return resourceId;
    }

    public void setResourceId(ResourceId resourceId) {
        this.resourceId = resourceId;
    }

}
