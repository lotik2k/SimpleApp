package com.movista.app.data.repository.main

import io.reactivex.Single

interface IMainRepository {

    fun isFirstStart(): Boolean
    fun authorizeUser(): Single<Unit>
    fun tokenExists(): Boolean
}