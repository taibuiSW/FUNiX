package com.funix.prm391x.se00255x.funix;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

class VolleyMgr {

    @SuppressLint("StaticFieldLeak")
    private static VolleyMgr mInstance;
    private static Context mCtx;

    private RequestQueue mRequestQueue;

    private VolleyMgr(Context context) {
        mCtx = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
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
}