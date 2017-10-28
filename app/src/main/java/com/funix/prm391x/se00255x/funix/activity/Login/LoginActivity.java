package com.funix.prm391x.se00255x.funix.activity.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.activity.VideoListing.VideoListingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Context mCtx;
    private CallbackManager mCallbackMgr;
    private LoginButton mLoginButton;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCtx = LoginActivity.this;

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // redirect to VideoListingActivity if user's already logged in
        if (mAuth.getCurrentUser() != null) {
            startMainActivity();
            return;
        }

        // [START initialize Fb login]
        // Initialize Facebook Login button
        mCallbackMgr = CallbackManager.Factory.create();
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setReadPermissions("email", "public_profile");
        mLoginButton.registerCallback(mCallbackMgr, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProgress = ProgressDialog.show(mCtx, "logging firebase", "please wait...");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(mCtx, "Login cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(mCtx, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // [END initialize Fb login]
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackMgr.onActivityResult(requestCode, resultCode, data);
    }

    // Using the token received from Facebook to sign in Firebase
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgress.dismiss();
                            startMainActivity();
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                Toast.makeText(mCtx, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void startMainActivity() {
        // finish LoginActivity
        finish();
        // start VideoListingActivity
        startActivity(new Intent(mCtx, VideoListingActivity.class));
    }

}
