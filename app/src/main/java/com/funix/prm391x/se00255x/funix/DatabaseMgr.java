package com.funix.prm391x.se00255x.funix;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

public class DatabaseMgr {
    private static String PLAYLIST = "playlist";
    private static String HISTORY  = "history";

    private static DatabaseMgr mInstance;
    private DatabaseReference mRefPlaylist;
    private DatabaseReference mRefHistory;


    public static DatabaseMgr getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseMgr();
        }
        return mInstance;
    }

    private DatabaseMgr() {
        FirebaseDatabase mFireDB = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference ref = mFireDB.getReference(user.getUid());
            mRefPlaylist = ref.child(PLAYLIST);
            mRefHistory  = ref.child(HISTORY);
        }
    }

    public void addToPlaylist(int seq, Video video) {
        mRefPlaylist.child("" + seq).setValue(video);
    }

    public Query getPlaylist() {
        return mRefPlaylist;
    }

    public void modifyHistory(Video video) {
        video.mTime = 9_999_999_999_999L - Calendar.getInstance().getTimeInMillis();
        mRefHistory.child(video.mId).setValue(video);
    }

    public Query getHistory() {
        return mRefHistory.orderByChild("mTime");
    }
}
