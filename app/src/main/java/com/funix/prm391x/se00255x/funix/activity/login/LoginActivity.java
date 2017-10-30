package com.funix.prm391x.se00255x.funix.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.funix.prm391x.se00255x.funix.R;
import com.funix.prm391x.se00255x.funix.activity.main.MainActivity;

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
        mPresenter.forwardResultToFbSdk(requestCode, resultCode, data);
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

    @Override
    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        mProgress = ProgressDialog.show(this, "logging firebase", "please wait...");
    }

    @Override
    public void hideProgress() {
        mProgress.dismiss();
    }

}
