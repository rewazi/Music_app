package com.example.musicapp.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("music_app_prefs", Context.MODE_PRIVATE)

    fun saveUsername(username: String) {
        prefs.edit().putString("username", username).apply()
    }

    fun saveUserId(userId: Int) {
        prefs.edit().putInt("user_id", userId).apply()
    }

    fun getUsername(): String {
        return prefs.getString("username", "") ?: ""
    }

    fun getUserId(): Int {
        return prefs.getInt("user_id", -1)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
