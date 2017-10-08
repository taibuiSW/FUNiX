package com.funix.prm391x.se00255x.funix;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DatabaseMgr {
    private static final String ROOT_HISTORY = "history";

    private static DatabaseMgr mInstance;

    private FirebaseDatabase mFireDB;
    private DatabaseReference mRef;

    public static DatabaseMgr getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseMgr();
        }
        return mInstance;
    }

    private DatabaseMgr() {
        mFireDB = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.e("___userId", user.getUid());
            mRef = mFireDB.getReference(ROOT_HISTORY).child(user.getUid());
        }
    }

    public void modifyHistory(Video video) {
        video.mTime = 9999999999999L - Calendar.getInstance().getTimeInMillis();
        mRef.child(video.mId).setValue(video);
    }

    public void getHistory(final MainActivity.CustomAdapter adapter) {
        mRef.orderByChild("mTime").addChildEventListener(new ChildAddedListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add(dataSnapshot.getValue(Video.class));
            }
        });
    }
}
