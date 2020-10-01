package com.example.dayplanner

import android.content.Context
import android.widget.Toast

public fun showToast(context: Context, text: String, time: Int){
    Toast.makeText(context, text, time).show()
}
public fun showToast(context: Context, resId: Int, time: Int){
    Toast.makeText(context, resId, time).show()
}