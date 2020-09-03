package com.example.dayplanner.db.addspending;

import android.provider.BaseColumns;

public class SuggestionsContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SuggestionsContract() {}

    /* Inner class that defines the table contents */
    public static class Suggestion implements BaseColumns {
        public static final String TABLE_NAME = "Suggestions";
        public static final String COLUMN_NAME_TITLE = "name";
        public static final String COLUMN_NAME_USAGE = "usage";
    }
}
