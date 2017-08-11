package com.funix.prm391x.se00255x.funix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class DatabaseMgr extends SQLiteOpenHelper {
    private static final String DB_NAME = "allAndEverything.db";
    // user account table
    private static final String TABLE_ACCOUNT = "account";
    private static final String ACC_USERNAME = "_username";
    private static final String ACC_PASSWORD = "password";
    // history table
    private static final String HIS_VIDEO_ID = "id";
    private static final String HIS_VIDEO_TITLE = "title";

    private String mUsername;

    DatabaseMgr(Context context, String username) {
        super(context, DB_NAME, null, 1);
        this.mUsername = username;
    }

    DatabaseMgr(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_ACCOUNT + " ("
                + ACC_USERNAME + " VARCHAR(225) PRIMARY KEY, " + ACC_PASSWORD + " VARCHAR(225));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + mUsername);
        onCreate(db);
    }

    void insert(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACC_USERNAME, username);
        values.put(ACC_PASSWORD, password);
        db.insert(TABLE_ACCOUNT, null, values);
        db.execSQL("CREATE TABLE " + username + " (" + HIS_VIDEO_ID + " CHAR(11), "
                + HIS_VIDEO_TITLE + " TEXT);");
    }

    int check(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT, new String[]{ACC_USERNAME, ACC_PASSWORD},
                ACC_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor.getCount() < 1) {
            //username is not exists
            cursor.close();
            return -1;
        } else {
            cursor.moveToNext();
            String passwordInDB = cursor.getString(1);
            cursor.close();
            if (password.equals(passwordInDB)) {
                //right password
                return 1;
            } else {
                //wrong password
                return 0;
            }
        }
    }

    void modifyHistory(Video video) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(mUsername, new String[]{HIS_VIDEO_ID},
                HIS_VIDEO_ID + "=?", new String[]{video.getId()}, null, null, null);
        if (cursor.getCount() > 0) {
            db.delete(mUsername, HIS_VIDEO_ID + "=?", new String[]{video.getId()});
        }
        cursor.close();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HIS_VIDEO_ID, video.getId());
        contentValues.put(HIS_VIDEO_TITLE, video.getTitle());
        db.insert(mUsername, null, contentValues);
    }

    ArrayList<Video> getHistory() {
        ArrayList<Video> history = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(mUsername, new String[]{HIS_VIDEO_ID, HIS_VIDEO_TITLE},
                null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                String id = cursor.getString(cursor.getColumnIndex(HIS_VIDEO_ID));
                String title = cursor.getString(cursor.getColumnIndex(HIS_VIDEO_TITLE));
                history.add(new Video(title, id));
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return history;
    }
}
