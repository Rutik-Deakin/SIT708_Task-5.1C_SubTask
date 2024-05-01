package com.example.videoplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PlaylistService extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PlaylistDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PLAYLISTS = "playlists";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_YOUTUBE_LINKS = "youtube_links";

    public PlaylistService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_PLAYLISTS + " (" +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_YOUTUBE_LINKS + " TEXT" + ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        onCreate(db);
    }

    // Method to add a new playlist to the database
    public long addPlaylist(long userId, String youtubeLinks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_YOUTUBE_LINKS, youtubeLinks);
        long newRowId = db.insert(TABLE_PLAYLISTS, null, values);
        db.close();
        return newRowId;
    }


    // Method to retrieve playlist data based on user ID
    public List<String> getPlaylistByUserId(long userId) {
        List<String> playlist = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_YOUTUBE_LINKS};
        String selection = COLUMN_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_PLAYLISTS, projection, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String link = cursor.getString(cursor.getColumnIndex(COLUMN_YOUTUBE_LINKS));
                playlist.add(link);
            }
            cursor.close();
        }
        db.close();
        return playlist;
    }
}
