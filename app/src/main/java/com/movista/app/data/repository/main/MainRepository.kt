package com.movista.app.data.repository.main

import com.google.gson.JsonObject
import com.movista.app.data.source.local.ILocalDataSource
import com.movista.app.data.source.remote.IRemoteDataSource
import com.movista.app.utils.EMPTY
import io.reactivex.Single

class MainRepository(
        private val remoteDataSource: IRemoteDataSource,
        private val localDataSource: ILocalDataSource
) : IMainRepository {

    companion object {

        private const val apiStub = "example"
    }

    override fun isFirstStart(): Boolean {
        return if (localDataSource.isFirstStart()) {
            localDataSource.makeNotFirstStart()
            true
        } else {
            false
        }
    }

    // заглушка для будущей реализации получения токена
    override fun authorizeUser(): Single<Unit> {

        val jO = JsonObject()
        jO.addProperty("username", apiStub)
        jO.addProperty("password", apiStub)

        return remoteDataSource.sendDeviceID(jO)
                .flatMap { remoteDataSource.getToken(apiStub, apiStub) }
                .map { Unit }
    }

    override fun tokenExists() = localDataSource.getToken() != String.EMPTY
}