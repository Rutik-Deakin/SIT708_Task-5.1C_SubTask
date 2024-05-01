package com.example.videoplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserService extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_FULLNAME = "fullname";
    private static final String COLUMN_PASSWORD = "password";

    public UserService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY," +
                COLUMN_USERNAME + " TEXT UNIQUE," +
                COLUMN_FULLNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(createTableQuery);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to add a new user to the database
    public long addUser(String username, String fullname, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_FULLNAME, fullname);
        values.put(COLUMN_PASSWORD, password);
        long userId = System.currentTimeMillis(); // Unique user ID using current time
        values.put(COLUMN_USER_ID, userId);
        long newRowId = db.insert(TABLE_USERS, null, values);
        db.close();
        return userId;
    }
    public User loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            long userId = cursor.getLong(cursor.getColumnIndex(COLUMN_USER_ID));
            String fullname = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME));
            user = new User(userId, username, fullname, password);
            cursor.close();
        } else {
            user = new User(0, null, null, null);
        }
        db.close();
        return user;
    }
}
