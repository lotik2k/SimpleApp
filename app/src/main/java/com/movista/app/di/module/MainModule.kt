package com.movista.app.di.module

import com.movista.app.data.repository.main.IMainRepository
import com.movista.app.data.repository.main.MainRepository
import com.movista.app.data.source.local.ILocalDataSource
import com.movista.app.data.source.remote.IRemoteDataSource
import com.movista.app.presentation.main.MainPresenter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module(includes = [NavigationModule::class])
class MainModule {

    @Provides
    @Singleton
    fun provideMainPresenter(router: Router, repository: IMainRepository): MainPresenter {
        return MainPresenter(router, repository)
    }

    @Provides
    @Singleton
    fun provideMainRepository(
            localDataSource: ILocalDataSource,
            remoteDataSource: IRemoteDataSource
    ): IMainRepository {
        return MainRepository(remoteDataSource, localDataSource)
    }
}