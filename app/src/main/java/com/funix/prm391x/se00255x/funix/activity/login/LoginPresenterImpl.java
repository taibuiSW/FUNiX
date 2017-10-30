package com.funix.prm391x.se00255x.funix.activity.login;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
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
        // Initialize Facebook Login button
        mCallbackMgr = CallbackManager.Factory.create();
        LoginButton loginBtn = mLoginView.getLoginButton();
        loginBtn.setReadPermissions("email", "public_profile");
        loginBtn.registerCallback(mCallbackMgr, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLoginView.showProgress();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                mLoginView.toastMessage("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                mLoginView.toastMessage(error.getMessage());
            }
        });
        // [END initialize Fb login]
    }

    // Using the token received from Facebook to sign in Firebase
    @Override
    public void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mLoginView.hideProgress();
                            mLoginView.startMainActivity();
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                mLoginView.toastMessage(e.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass the activity result back to the Facebook SDK
        mCallbackMgr.onActivityResult(requestCode, resultCode, data);
    }
}