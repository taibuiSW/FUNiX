package com.funix.prm391x.se00255x.funix;

import android.content.res.Configuration;

public interface ViewListFragmentView extends ViewMvp, VideoListing {

    void onOrientationChanged(Configuration newConfig);

}
