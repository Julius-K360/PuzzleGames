package com.kotlincodes.sharedpreferenceswithkotlin

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context){
    private val PREFERENCE_ISGAMEALIVE = "IsGameAlive"
    private val PREFERENCE_COUNTER = "Counter"
    val preference_isGameAlive = context.getSharedPreferences(PREFERENCE_ISGAMEALIVE, Context.MODE_PRIVATE)
    val preference_counter = context.getSharedPreferences(PREFERENCE_COUNTER, Context.MODE_PRIVATE)


    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = preference_counter.edit()

        editor.putInt(KEY_NAME, value)

        editor.commit()

    }
    fun getValueInt(KEY_NAME: String): Int {

        return preference_counter.getInt(KEY_NAME, 0)
    }

    fun getValueBoolien(KEY_NAME: String, defaultValue: Boolean): Boolean {

        return preference_isGameAlive.getBoolean(KEY_NAME, defaultValue)

    }

    fun save(KEY_NAME: String, status: Boolean) {

        val editor: SharedPreferences.Editor = preference_isGameAlive.edit()

        editor.putBoolean(KEY_NAME, status!!)

        editor.commit()
    }
}