package com.example.dayplanner.db.budgets;

import android.provider.BaseColumns;

public class BudgetNamesContract {
    public static class Budget implements BaseColumns{
        public static final String TABLE_NAME = "bugdets";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SORT_DATE = "sortDate";
    }
}
