package com.funix.prm391x.se00255x.funix;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentSignUp extends Fragment {
    EditText mEdtEmail;
    EditText mEdtPassword;
    Button mBtnSignUp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        mEdtEmail = (EditText) view.findViewById(R.id.edt_email);
        Log.e("___email", "" + (mEdtEmail ==null));
        mEdtPassword = (EditText) view.findViewById(R.id.edt_password);
        Log.e("___password", "" + (mEdtPassword ==null));
        mBtnSignUp = (Button) view.findViewById(R.id.btn_sign_up);
        Log.e("___btn", "" + (mBtnSignUp ==null));
        return view;
    }
}
