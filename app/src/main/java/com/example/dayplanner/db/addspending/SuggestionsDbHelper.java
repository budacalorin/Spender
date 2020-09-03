package com.example.dayplanner.db.addspending;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dayplanner.db.addspending.SuggestionsContract;

public class SuggestionsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Suggestions.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SuggestionsContract.Suggestion.TABLE_NAME + " (" +
                    SuggestionsContract.Suggestion._ID + " INTEGER PRIMARY KEY," +
                    SuggestionsContract.Suggestion.COLUMN_NAME_TITLE + " TEXT," +
                    SuggestionsContract.Suggestion.COLUMN_NAME_USAGE+ " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SuggestionsContract.Suggestion.TABLE_NAME;

    public SuggestionsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertData(String name, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SuggestionsContract.Suggestion.COLUMN_NAME_TITLE, name);
        contentValues.put(SuggestionsContract.Suggestion.COLUMN_NAME_USAGE, value);
        long index = db.insert(SuggestionsContract.Suggestion.TABLE_NAME,null, contentValues);
        return index;
    }
}
