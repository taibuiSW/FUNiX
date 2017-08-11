package com.funix.prm391x.se00255x.funix;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_IN = "Log In";
    private static final String SIGN_UP = "Sign Up";
    private static final String PATTERN = "[a-z_0-9]{5,}";

    private boolean mLogIn = true;
    private ConstraintLayout mLayout;
    private EditText mEdtUsername;
    private EditText mEdtPassword;
    private EditText mEdtConfirmPassword;
    private TextView mTxvRule;
    private Button mBtnLeft;
    private Button mBtnRight;
    private DatabaseMgr mDatabase;
    private SimpleToast mToast = new SimpleToast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Log in");

        mLayout = (ConstraintLayout) findViewById(R.id.layout_log_in);
        mEdtUsername = (EditText) findViewById(R.id.edt_username);
        mEdtPassword = (EditText) findViewById(R.id.edt_password);
        mEdtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
        mTxvRule = (TextView) findViewById(R.id.txv_rule);
        mBtnLeft = (Button) findViewById(R.id.btn_left);
        mBtnRight = (Button) findViewById(R.id.btn_right);
        mDatabase = new DatabaseMgr(this);
        setBtnLeftClick();
        setBtnRightClick();
    }

    private void setBtnLeftClick() {
        mBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLogIn) {
                    switchToSignUpMode();
                } else {
                    switchToLogInMode();
                }
            }
        });
    }

    private void setBtnRightClick() {
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEdtUsername.getText().toString().trim();
                String password = mEdtPassword.getText().toString().trim();
                if (!username.matches(PATTERN) || !password.matches(PATTERN)) {
                    mToast.makeToast(getString(R.string.rule));
                    return;
                }
                int result = mDatabase.check(username, password);
                if (mLogIn) {
                    switch (result) {
                        case 1:
                            mToast.makeToast("Log in successfully!");
                            startMainActivity(username);
                            break;
                        case 0:
                            mToast.makeToast("The password you entered is invalid");
                            break;
                        case -1:
                            mToast.makeToast("The username you entered does not exist");
                    }
                } else {
                    String confirmPassword = mEdtConfirmPassword.getText().toString();
                    if (!confirmPassword.equals(password)) {
                        mToast.makeToast("Passwords don't match");
                        return;
                    }
                    if (result == -1) {
                        mDatabase.insert(username, password);
                        mToast.makeToast("Thank you for registering");
                        switchToLogInMode();
                    } else {
                        mToast.makeToast("The username you entered already exists");
                    }
                }

            }
        });
    }

    private void switchToSignUpMode() {
        setTitle(SIGN_UP);
        mBtnLeft.setText(LOG_IN);
        mEdtConfirmPassword.setVisibility(View.VISIBLE);
        mTxvRule.setVisibility(View.VISIBLE);
        ConstraintSet set = new ConstraintSet();
        set.clone(mLayout);
        set.connect(R.id.btn_left, ConstraintSet.TOP, R.id.txv_rule, ConstraintSet.BOTTOM, 32);
        set.connect(R.id.btn_right, ConstraintSet.TOP, R.id.txv_rule, ConstraintSet.BOTTOM, 32);
        set.applyTo(mLayout);
        mLogIn = false;
    }

    private void switchToLogInMode() {
        setTitle(LOG_IN);
        mBtnLeft.setText(SIGN_UP);
        mEdtUsername.setText("");
        mEdtPassword.setText("");
        mEdtConfirmPassword.setText("");
        ConstraintSet set = new ConstraintSet();
        set.clone(mLayout);
        set.applyTo(mLayout);
        mEdtConfirmPassword.setVisibility(View.GONE);
        mTxvRule.setVisibility(View.GONE);
        mLogIn = true;
    }

    private void startMainActivity(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private class SimpleToast {
        void makeToast(String message) {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
