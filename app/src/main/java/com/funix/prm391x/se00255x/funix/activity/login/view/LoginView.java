package com.funix.prm391x.se00255x.funix.activity.login.view;

import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public interface LoginView extends FacebookCallback<LoginResult>, OnCompleteListener<AuthResult> {

    LoginButton getLoginButton();

    void startMainActivity();

}
