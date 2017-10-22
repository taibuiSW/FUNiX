package com.funix.prm391x.se00255x.funix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

class VolleyMgr {

    @SuppressLint("StaticFieldLeak")
    private static VolleyMgr mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleyMgr(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<>(calcCacheSize());

                    // using 12.5% of maxMemory, each image'size is around 256KiB
                    private int calcCacheSize() {
                        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                        return maxMemory / 8 / 256;
                    }

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleyMgr getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyMgr(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx);
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}