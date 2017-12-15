package com.funix.prm391x.se00255x.funix.data;

import com.funix.prm391x.se00255x.funix.data.model.Video;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

public class DatabaseMgr {
    private static final String PLAYLIST = "playlist";
    private static final String HISTORY  = "history";

    private static DatabaseMgr mInstance;

    private DatabaseReference mRefPlaylist;
    private DatabaseReference mRefHistory;


    public static DatabaseMgr getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseMgr();
        }
        return mInstance;
    }

    public static void clearInstance() {
        mInstance = null;
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

    public void clearPlaylist() {
        mRefPlaylist.removeValue();
    }

    public void updateHistory(Video video) {
        video.setTime(9_999_999_999_999L - Calendar.getInstance().getTimeInMillis());
        mRefHistory.child(video.getId()).setValue(video);
    }

    public Query getQuery(String queryKey) {
        switch (queryKey) {
            case HISTORY:
                return mRefHistory.orderByChild("time");
            case PLAYLIST:
                return mRefPlaylist;
        }
        return null;
    }
}
