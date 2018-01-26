package com.funix.prm391x.se00255x.funix.activity.login.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.activity.login.presenter.LoginPresenter;
import com.funix.prm391x.se00255x.funix.activity.login.presenter.LoginPresenterImpl;
import com.funix.prm391x.se00255x.funix.activity.main.view.MainActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private LoginPresenter mPresenter;
    private LoginButton mLoginButton;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = findViewById(R.id.login_button);
        mPresenter = new LoginPresenterImpl(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mPresenter.getCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public LoginButton getLoginButton() {
        return mLoginButton;
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    // Handle facebook login result
    @Override
    public void onSuccess(LoginResult loginResult) {
        showProgress();
        mPresenter.handleFacebookAccessToken(loginResult.getAccessToken());
    }

    @Override
    public void onCancel() {
        toastMessage("Login cancelled");
    }

    @Override
    public void onError(FacebookException error) {
        toastMessage(error.getMessage());
    }

    // Handle firebase login result
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            hideProgress();
            startMainActivity();
        } else {
            Exception e = task.getException();
            if (e != null) {
                toastMessage(e.getMessage());
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showProgress() {
        mProgress = ProgressDialog.show(this, "logging in firebase", "please wait...");
    }

    private void hideProgress() {
        mProgress.dismiss();
    }
}
