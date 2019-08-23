package com.movista.app.domain.searchparams

import com.movista.app.data.entity.PopularResponse
import com.movista.app.data.entity.SearchResponse
import com.movista.app.domain.model.SearchModel
import io.reactivex.Single

interface ISearchParamsRepository {

    fun setPassengersInfo()
    fun setTripTypes()
    fun getSearchModel(): SearchModel
    fun search(chars: String): Single<SearchResponse>
    fun getPlaceInfo(latitude: Double, longitude: Double): Single<SearchResponse>
    fun getPopular(): Single<PopularResponse>
}