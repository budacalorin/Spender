package com.example.dayplanner.db.spender;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SpenderItemsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static String DATABASE_NAME;
    public static SpenderItemsContract spenderItemsContract;

    private static String SQL_CREATE_ENTRIES;

    private static String SQL_DELETE_ENTRIES;

    public SpenderItemsDbHelper(Context context,SpenderItemsContract itemsContract) {
        super(context, itemsContract.TABLE_NAME+".db", null, DATABASE_VERSION);
        spenderItemsContract = itemsContract;
        DATABASE_NAME = spenderItemsContract.TABLE_NAME + ".db";
        SQL_CREATE_ENTRIES =
                "CREATE TABLE " + spenderItemsContract.TABLE_NAME + " (" +
                        SpenderItemsContract.SpenderItem._ID + " INTEGER PRIMARY KEY," +
                        SpenderItemsContract.SpenderItem.COLUMN_NAME_TITLE + " TEXT," +
                        SpenderItemsContract.SpenderItem.COLUMN_NAME_VALUE + " TEXT," +
                        SpenderItemsContract.SpenderItem.COLUMN_NAME_DETAILS + " TEXT," +
                        SpenderItemsContract.SpenderItem.COLUMN_NAME_DATE + " TEXT)";
        SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + spenderItemsContract.TABLE_NAME;

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

    public long insertData(String name, String value, String details, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SpenderItemsContract.SpenderItem.COLUMN_NAME_TITLE, name);
        contentValues.put(SpenderItemsContract.SpenderItem.COLUMN_NAME_VALUE, value);
        contentValues.put(SpenderItemsContract.SpenderItem.COLUMN_NAME_DETAILS,details);
        contentValues.put(SpenderItemsContract.SpenderItem.COLUMN_NAME_DATE,date);
        long index = db.insert(spenderItemsContract.TABLE_NAME,null, contentValues);
        return index;
    }
}
