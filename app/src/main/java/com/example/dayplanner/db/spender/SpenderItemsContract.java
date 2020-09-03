package com.example.dayplanner.db.spender;

import android.provider.BaseColumns;

public class SpenderItemsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    public String TABLE_NAME = "";
    public SpenderItemsContract(String name) {TABLE_NAME = name;}

    /* Inner class that defines the table contents */
    public static class SpenderItem implements BaseColumns {
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_DETAILS = "detailsss";
        public static final String COLUMN_NAME_DATE = "date";
        public static final Integer COLUMN_INDEX_TITLE = 1;
        public static final Integer COLUMN_INDEX_VALUE = 2;
        public static final Integer COLUMN_INDEX_DETAILS = 3;
        public static final Integer COLUMN_INDEX_DATE = 4;
    }
}
