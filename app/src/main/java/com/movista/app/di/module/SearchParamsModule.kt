package com.movista.app.di.module

import android.content.res.Resources
import com.movista.app.data.repository.search.SearchParamsRepository
import com.movista.app.data.source.remote.IRemoteDataSource
import com.movista.app.domain.model.SearchModel
import com.movista.app.domain.searchparams.*
import com.movista.app.presentation.converter.SearchConverter
import com.movista.app.presentation.searchparams.dateselect.DateSelectPresenter
import com.movista.app.presentation.searchparams.placesearch.PlaceSearchPresenter
import com.movista.app.presentation.searchparams.searchparams.SearchParamsPresenter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router
import javax.inject.Singleton


@Module
class SearchParamsModule {

    @Provides
    @Singleton
    fun provideSearchUseCase(repository: ISearchParamsRepository): SearchParamsUseCase {
        return SearchParamsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchPresenter(
            router: Router,
            searchUseCase: SearchParamsUseCase,
            converter: com.movista.app.presentation.converter.SearchConverter,
            resources: Resources
    ): SearchParamsPresenter {
        return SearchParamsPresenter(router, searchUseCase, converter, resources)
    }

    @Provides
    fun providePlaceSearchPresenter(
            router: Router,
            useCase: IPlaceSearchUseCase,
            converter: SearchConverter,
            resources: Resources
    ): PlaceSearchPresenter {
        return PlaceSearchPresenter(router, useCase, converter, resources)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
            remoteDataSource: IRemoteDataSource,
            model: SearchModel
    ): ISearchParamsRepository {
        return SearchParamsRepository(remoteDataSource, model)
    }

    @Provides
    @Singleton
    fun provideEmptySearchModel(): SearchModel {
        return SearchModel()
    }

    @Provides
    @Singleton
    fun providePlaceSearchConverter(resources: Resources): SearchConverter {
        return SearchConverter(resources)
    }

    @Provides
    @Singleton
    fun provideDateSelectPresenter(
            router: Router,
            resources: Resources,
            useCase: IDateSelectUseCase
    ): DateSelectPresenter {
        return DateSelectPresenter(router, resources, useCase)
    }

    @Provides
    @Singleton
    fun providePlaceSearchUseCase(
            searchUseCase: SearchParamsUseCase
    ): IPlaceSearchUseCase {
        return searchUseCase
    }

    @Provides
    @Singleton
    fun provideDateSelectUseCase(
            searchUseCase: SearchParamsUseCase
    ): IDateSelectUseCase {
        return searchUseCase
    }


    @Provides
    @Singleton
    fun providePassengersInfoUseCase(
            searchUseCase: SearchParamsUseCase
    ): IPassengersUseCase {
        return searchUseCase
    }

    @Provides
    @Singleton
    fun provideComfortTypeUseCase(
            searchUseCase: SearchParamsUseCase
    ): IComfortTypeUseCase {
        return searchUseCase
    }

}