package com.movista.app.domain.searchparams

import com.movista.app.data.entity.PopularResponse
import com.movista.app.data.entity.SearchResponse
import io.reactivex.Single

interface IPlaceSearchUseCase {

    fun setFromCity(name: String, lat: Double, lon: Double)
    fun setToCity(name: String, lat: Double, lon: Double)
    fun search(chars: String): Single<SearchResponse>
    fun getPlaceInfo(latitude: Double, longitude: Double): Single<SearchResponse>
    fun getPopular(): Single<PopularResponse>
//    fun getRecent(): Single<RecentResponse>
}