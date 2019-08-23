package com.movista.app.data.source.local

interface ILocalDataSource {

    fun isFirstStart(): Boolean
    fun makeNotFirstStart()
    fun getToken(): String
    fun saveToken(token: String)
}