package com.funix.prm391x.se00255x.funix.activity.login.presenter;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.funix.prm391x.se00255x.funix.activity.login.view.LoginView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mLoginView;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackMgr;

    public LoginPresenterImpl(LoginView loginView) {
        mLoginView = loginView;
        mAuth = FirebaseAuth.getInstance();

        // redirect to MainActivity if user's already logged in
        if (mAuth.getCurrentUser() != null) {
            mLoginView.startMainActivity();
            return;
        }

        // [START initialize Fb login]
        mCallbackMgr = CallbackManager.Factory.create();

        // Initialize Facebook Login button
        LoginButton loginBtn = mLoginView.getLoginButton();
        loginBtn.setReadPermissions("email", "public_profile");
        loginBtn.registerCallback(mCallbackMgr, mLoginView);
        loginBtn.performClick();
        // [END initialize Fb login]
    }

    // Using the token received from Facebook to sign in Firebase
    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(mLoginView);
    }

    @Override
    public CallbackManager getCallbackManager() {
        return mCallbackMgr;
    }
}
