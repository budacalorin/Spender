package com.example.dayplanner.chart;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.dayplanner.R
import com.example.dayplanner.db.spender.SpenderItemsContract
import com.example.dayplanner.spender.itemInList
import com.example.dayplanner.spender.myAdapterSpender
import com.example.dayplanner.utils.getCursorForDatabase
import com.example.dayplanner.utils.stringToDate
import java.util.*
import kotlin.collections.ArrayList

private fun getList(context: Context, dbNames: ArrayList<String>, category: String, begin: Date, end: Date ): ArrayList<itemInList>{
    var itemList = ArrayList<itemInList>()
    for (dbName in dbNames) {
        var cursor = getCursorForDatabase(dbName, context)
        var id = 0L
        while (cursor.moveToNext()) {
            var date = stringToDate(cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_DATE));
            if (date!!.after(end) || date.before(begin))
                continue

            val name: String = cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_TITLE);
            if (name != category)
                continue

            val value = java.lang.Double.parseDouble(cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_VALUE));
            val description = cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_DETAILS);
            val stringDate = cursor.getString(SpenderItemsContract.SpenderItem.COLUMN_INDEX_DATE);
            val listItem = itemInList(name, value, id, description, stringDate)

            itemList.add(listItem)
            id++
        }
        cursor.close()
    }
    return itemList;
}

fun getSliceInfoAlertDialog(context: Context, dbName: ArrayList<String>, category: String, begin: Date, end: Date ): AlertDialog? {
    val list = getList(context, dbName, category, begin, end)
    val inflater: LayoutInflater? = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

    list.sortBy { it.value }

    if (inflater == null)
        return null;

    val view: View = inflater.inflate(R.layout.slice_info_dialog, null)

    val categoryTextView = view.findViewById<TextView>(R.id.slice_info_dialog_category_TextView)
    categoryTextView.setText("Category: ${category}");

    val listView = view.findViewById<ListView>(R.id.slice_info_dialog_listView)
    val adapter = myAdapterSpender(context, list)

    for ( i in 0 until list.size)
        if(list[i].details.isNotBlank())
            adapter.getSelected().add(i.toLong())

    listView.adapter = adapter;

    listView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
        if (adapter.selected.contains(position.toLong())) {
            adapter.selected.remove(position.toLong())
        } else {
            adapter.selected.add(position.toLong())
        }
        adapter.notifyDataSetChanged()
    })

    val totalTextView = view.findViewById<TextView>(R.id.slice_info_dialog_total_textView)

    totalTextView.setText("Total: ${totalValueOfList(list).toString()}")

    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context);

    alertDialogBuilder.setView(view)

    return alertDialogBuilder.create()
}

fun totalValueOfList(list: ArrayList<itemInList>): Double {
    var sum: Double = 0.0
    for ( item in list ){
        sum += item.value
    }
    return sum;
}


