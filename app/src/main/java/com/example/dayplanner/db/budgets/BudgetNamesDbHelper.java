package com.example.dayplanner.db.budgets;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dayplanner.db.budgets.BudgetNamesContract;

public class BudgetNamesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=2;
    public static final String DATABASE_NAME = "budgets.db";

    public static final String SQL_CREATE_ENTITIES = "CREATE TABLE " +
            BudgetNamesContract.Budget.TABLE_NAME + " (" +
            BudgetNamesContract.Budget._ID + " INTEGER PRIMARY KEY," +
            BudgetNamesContract.Budget.COLUMN_NAME_NAME + " TEXT," +
            BudgetNamesContract.Budget.COLUMN_NAME_SORT_DATE + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
            BudgetNamesContract.Budget.TABLE_NAME;


    public BudgetNamesDbHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTITIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public long insertData (String name,boolean sortDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BudgetNamesContract.Budget.COLUMN_NAME_NAME,name);
        cv.put(BudgetNamesContract.Budget.COLUMN_NAME_SORT_DATE,sortDate);
        long index;
        index = db.insert(BudgetNamesContract.Budget.TABLE_NAME,null,cv);
        return index;
    }
}

