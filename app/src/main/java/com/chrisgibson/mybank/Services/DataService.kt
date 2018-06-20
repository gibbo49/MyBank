package com.chrisgibson.mybank.Services

import android.graphics.Color
import com.chrisgibson.mybank.Model.Item
import com.chrisgibson.mybank.Utilities.*

object DataService {


    fun colorList():ArrayList<Int>{
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#448aff")) //blue - auto
        colors.add(Color.parseColor("#000000")) //black - work
        colors.add(Color.parseColor("#e91e63")) //pink - mortgage
        colors.add(Color.parseColor("#9e9e9e")) //eatingout -grey
        colors.add(Color.parseColor("#f44336")) //bills - red
        colors.add(Color.parseColor("#009688")) //education - green
        colors.add(Color.parseColor("#cddc39")) //entertainment - yellow
        colors.add(Color.parseColor("#9c27b0")) // groceries - purple
        colors.add(Color.parseColor("#ff5722")) //homegoods - orange
        colors.add(Color.parseColor("#3f51b5")) //health - blue
        colors.add(Color.parseColor("#ffeb3b")) //clothing - yellow
        colors.add(Color.parseColor("#414141")) //other - grey
        return colors
    }



    val options = arrayOf(RENT, AUTOMOTIVE, BILLS, HOME_GOODS, ENTERTAINMENT, HEALTH, GROCERIES, EAT, WORK, EDUCATIONAL, CLOTHING, OTHER)

}