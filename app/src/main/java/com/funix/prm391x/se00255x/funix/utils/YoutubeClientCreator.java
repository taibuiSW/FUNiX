package com.funix.prm391x.se00255x.funix.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class YoutubeClientCreator {

    private static final String BASE_URL = "https://www.googleapis.com/youtube/";

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    private static Retrofit mRetrofit =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(mOkHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

    public static YoutubeClient create() {
        return mRetrofit.create(YoutubeClient.class);
    }
}
