package com.example.dayplanner

import java.io.BufferedReader
import java.io.File

object ColorManager {
    val colorsFile = File("colors.txt")
    val colorsMap = HashMap<String, String>()
    val accentColorKey = "accent"
    val primaryColorKey = "primary"
    val primaryDarkColor = "primary_dark"


    fun initColors(){
        if (colorsMap.size != 0){
            return
        }

        colorsFile.forEachLine {line : String ->

        }
    }

}