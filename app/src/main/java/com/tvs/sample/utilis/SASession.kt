package com.tvs.sample.utilis

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tvs.sample.entities.UserData

/**
 * Created by selva
 */

class SASession(aCtx: Context) {

    // ---Initialize Shared preference---
    protected lateinit var myPreference: SharedPreferences
    protected lateinit var myEditor: SharedPreferences.Editor

    // ---Assign value---
    val loginStatus: Boolean
        get() = myPreference.getBoolean("Login", false)


    init {
        try {
            myPreference = PreferenceManager.getDefaultSharedPreferences(aCtx)
            myEditor = myPreference.edit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun putLoginStatus(aStatus: Boolean) {
        // ---Put string---
        myEditor.putBoolean("Login", aStatus)
        myEditor.commit()
    }


    fun saveListData(aStatus: List<UserData>) {
        // ---Put string---
        var gson = Gson();
        var json = gson.toJson(aStatus); // myObject - instance of MyObject
        myEditor.putString("UserData", json)
        myEditor.commit()
    }

    fun getListData(): List<UserData> {
        // ---Put string---
        val typdde = object : TypeToken<List<UserData>>() {}.type
        val json = myPreference.getString("UserData", "");
        val obj = Gson().fromJson(json, typdde) as List<UserData>
        return obj;
    }

    // ---Assign value---


    fun clear() {
        myEditor.clear()
        myEditor.commit()
    }


}