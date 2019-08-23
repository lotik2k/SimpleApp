package com.movista.app.data.source.local

import android.content.SharedPreferences

class PrefsStorage(private val prefs: SharedPreferences) : ILocalDataSource {

    companion object {
        const val FIST_START_TAG = "isFirstStart"
        const val ACCESS_TOKEN_TAG = "accessToken"
    }

    override fun isFirstStart() = prefs.getBoolean(FIST_START_TAG, true)

    override fun makeNotFirstStart() {
        prefs.edit()
                .putBoolean(FIST_START_TAG, false)
                .apply()
    }

    override fun saveToken(token: String) {
        prefs.edit()
                .putString(ACCESS_TOKEN_TAG, token)
                .apply()
    }

    override fun getToken(): String = prefs.getString(ACCESS_TOKEN_TAG, /*String.EMPTY*/"stub!")!!
}
