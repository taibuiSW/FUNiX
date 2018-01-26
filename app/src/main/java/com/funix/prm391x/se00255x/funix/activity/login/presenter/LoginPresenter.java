package com.funix.prm391x.se00255x.funix.activity.login.presenter;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;

public interface LoginPresenter {

    void handleFacebookAccessToken(AccessToken token);

    CallbackManager getCallbackManager();

}
