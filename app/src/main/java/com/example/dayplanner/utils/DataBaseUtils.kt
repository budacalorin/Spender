package com.example.dayplanner.utils

import android.content.Context
import android.database.Cursor
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.dayplanner.db.spender.SpenderItemsContract
import com.example.dayplanner.db.spender.SpenderItemsDbHelper
import java.util.*
import kotlin.collections.ArrayList

fun getCursorForDatabase(dbName: String, context: Context): Cursor {
    val spenderItemsContract = SpenderItemsContract(dbName)
    val spenderItemsDbHelper = SpenderItemsDbHelper(context, spenderItemsContract)

    val db = spenderItemsDbHelper.readableDatabase

    return db.rawQuery("SELECT * FROM " + spenderItemsContract.TABLE_NAME, null)
}

fun dataToHashMap(dbName: String, begin: Date, end: Date, context: Context): HashMap<String, Double> {
    val cursor = getCursorForDatabase(dbName, context)
    val map = HashMap<String, Double>()

    while (cursor.moveToNext()) {
        val name = cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_TITLE)
        var value = java.lang.Double.parseDouble(cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_VALUE))
        val date = stringToDate(cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_DATE))

        if (date!!.after(end) || date.before(begin)) {
            continue
        }
        //if (value.doubleValue()>0)
        //  continue;

        val oldVal = map[name]
        if (oldVal != null) {
            value += oldVal
        }
        map[name] = value
    }
    cursor.close()

    return map
}

fun listToPie(data: ArrayList<DataEntry>, pie : Pie): Pie {
    pie.data(data)
    pie.title().fontSize(40)
    return pie
}

fun hashMapToList(map: HashMap<String, Double>, putAll: Boolean): ArrayList<DataEntry>{
    val data = ArrayList<DataEntry>()
    for (s in map.keys) {
        if (putAll){
            //experimental
            data.add(ValueDataEntry(s, (map[s])!!))
        } else if (map[s]!!.toDouble() < 0 ) {
                 data.add(ValueDataEntry(s, -(map[s])!!))
                }
    }
    return data;
}