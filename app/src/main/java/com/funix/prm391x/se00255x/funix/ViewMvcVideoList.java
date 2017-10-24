package com.funix.prm391x.se00255x.funix;

import android.support.v4.view.ViewPager;


public interface ViewMvcVideoList extends ViewMvc {
    ViewPager getViewPager();

    void indicateNetworkStatus(boolean isConnected);
}
