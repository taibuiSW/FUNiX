package com.funix.prm391x.se00255x.funix;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentLogin extends Fragment {
    EditText mEdtEmail;
    EditText mEdtPassword;
    Button mBtnLogin;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEdtEmail = (EditText) view.findViewById(R.id.edt_email);
        mEdtEmail.setText("silencewalker94@gmail.com");
        mEdtPassword = (EditText) view.findViewById(R.id.edt_password);
        mEdtPassword.setText("sims0dep");
        mBtnLogin = (Button) view.findViewById(R.id.btn_log_in);
        mAuth = FirebaseAuth.getInstance();
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = mEdtEmail.getText().toString().trim();
                String password = mEdtPassword.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String message = "Log in successfully!";
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch (Exception e) {
                                        message = e.getMessage();
                                    }
                                }
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        });
            }
        });
        return view;
    }
}

