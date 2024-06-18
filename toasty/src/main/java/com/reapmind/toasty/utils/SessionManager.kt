package com.reapmind.toasty.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences("app_name", Context.MODE_PRIVATE)

    companion object{
        const val SESSION_ID = "session_id"
    }
    fun saveSessionId(sessionId: String) {
        val editor = prefs.edit()
        editor.putString(SESSION_ID,sessionId)
        editor.apply()
    }

    fun getSessionId(): String? {
        return prefs.getString(SESSION_ID, null)
    }
}