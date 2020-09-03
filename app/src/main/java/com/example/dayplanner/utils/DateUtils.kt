package com.example.dayplanner.utils

import java.util.*
import kotlin.math.absoluteValue

fun stringToDate(stringDate: String) : Date? {
    val splited = stringDate.split("/")
    if (splited.size!=3)
        return null
    val day = splited[0].toInt()
    val month = splited[1].toInt() - 1
    val year = ( "20" + splited[2]).toInt()
    return Date(year,month, day)
}

fun dateToString(date: Date) : String {
    val day : String = if (date.date < 9) "0" + date.date else date.date.toString()
    val month : String = if (date.month +1 < 9) "0" + (date.month + 1) else (date.month + 1).toString()
    val year : String = (date.year % 100).toString()

    return "$day/$month/$year"
}

fun dayDiff(first: Date, second: Date): Int {
    return ((second.time - first.time) / 86400000L).toInt().absoluteValue // milliseconds in a day
}