package com.funix.prm391x.se00255x.funix.activity.login.presenter;

import android.content.Intent;

import com.facebook.AccessToken;

public interface LoginPresenter {

    void handleFacebookAccessToken(AccessToken token);

    void forwardResultToFbSdk(int requestCode, int resultCode, Intent data);

}
