package com.movista.app.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.movista.app.data.source.local.ILocalDataSource
import com.movista.app.data.source.local.PrefsStorage
import com.movista.app.data.source.remote.ApiEndpoints
import com.movista.app.data.source.remote.IRemoteDataSource
import com.movista.app.data.source.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: Application, val name: String) {

    @Provides
    @Singleton
    fun provideApplication() = application

    @Provides
    @Singleton
    fun provideResources() = application.resources

    @Provides
    @Singleton
    fun provideSharedPrefs(): SharedPreferences {
        return application.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(sharedPreferences: SharedPreferences): ILocalDataSource {
        return PrefsStorage(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(endpoint: ApiEndpoints): IRemoteDataSource {
        return RemoteDataSource(endpoint)
    }
}