package com.souqbh.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.google.gson.Gson
import com.souqbh.data.api.UserDataResponse
import com.souqbh.utils.constants.PrefsConstants
import com.souqbh.utils.constants.PrefsConstants.PREF_USER_DATA

class Prefs(context: Context) {

    private val TAG = "SouqBH_Prefs"

    internal var singleton: Prefs? = null

    private lateinit var preferences: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    init {
        preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        editor = preferences.edit()
    }

    companion object {
        var prefs: Prefs? = null

        fun getInstance(context: Context): Prefs? {
            prefs = if (prefs != null) prefs else Prefs(context)
            return prefs
        }
    }

    var userDataModel: UserDataResponse?
        get() = Gson().fromJson<UserDataResponse>(
            preferences.getString(PrefsConstants.PREF_USER_DATA, ""),
            UserDataResponse::class.java
        )
        set(userDataModel) = preferences.edit().putString(PREF_USER_DATA, Gson().toJson(userDataModel)).apply()

    fun save(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun save(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun save(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun save(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    fun save(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun save(key: String, value: Set<String>) {
        editor.putStringSet(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun getString(key: String, defValue: String): String? {
        return preferences.getString(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return preferences.getFloat(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun getStringSet(key: String, defValue: Set<String>): Set<String>? {
        return preferences.getStringSet(key, defValue)
    }

    fun getAll(): Map<String, *> {
        return preferences.all
    }

    fun remove(key: String) {
        editor.remove(key).apply()
    }

    fun clearAll(context: Context) {
        editor.clear()
        editor.apply()
    }

    private class Builder(context: Context?) {

        private val context: Context

        init {
            if (context == null) {
                throw IllegalArgumentException("Context must not be null.")
            }
            this.context = context.applicationContext
        }

        /**
         * Method that creates an instance of Prefs
         *
         * @return an instance of Prefs
         */
        fun build(): Prefs {
            return Prefs(context)
        }
    }
}