package com.movista.app.data.repository.search

import com.movista.app.App
import com.movista.app.data.entity.PopularResponse
import com.movista.app.data.entity.SearchResponse
import com.movista.app.data.source.remote.IRemoteDataSource
import com.movista.app.domain.model.SearchModel
import com.movista.app.domain.searchparams.ISearchParamsRepository
import com.movista.app.presentation.common.Error
import io.reactivex.Single

class SearchParamsRepository(
        private val remoteDataSource: IRemoteDataSource,
        private val searchModel: SearchModel
) : ISearchParamsRepository {

    override fun search(chars: String): Single<SearchResponse> {

        if (!App.isOnline()) return Single.error(Throwable(Error.NO_CONNECTION))

        return remoteDataSource.searchPlace(chars)
    }

    override fun getPlaceInfo(latitude: Double, longitude: Double): Single<SearchResponse> {

        if (!App.isOnline()) return Single.error(Throwable(Error.NO_CONNECTION))

        return remoteDataSource.searchPlaceByLocation(latitude, longitude)
    }

    override fun getSearchModel(): SearchModel {

        return searchModel
    }

    override fun setPassengersInfo() {
    }

    override fun setTripTypes() {
    }

    override fun getPopular(): Single<PopularResponse> {
        return remoteDataSource.getPopular()
    }
}