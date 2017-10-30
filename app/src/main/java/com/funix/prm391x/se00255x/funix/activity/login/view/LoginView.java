package com.funix.prm391x.se00255x.funix.activity.login.view;

import com.facebook.login.widget.LoginButton;

public interface LoginView {

    LoginButton getLoginButton();

    void startMainActivity();

    void toastMessage(String message);

    void showProgress();

    void hideProgress();
}
