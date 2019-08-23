package com.movista.app.data.source.remote

import com.google.gson.JsonObject
import com.movista.app.data.entity.PopularResponse
import com.movista.app.data.entity.SearchResponse
import com.movista.app.data.entity.TripsRequestModel
import com.movista.app.data.entity.TripsResponse
import io.reactivex.Single
import okhttp3.ResponseBody

interface IRemoteDataSource {

    fun sendDeviceID(jo: JsonObject): Single<ResponseBody>
    fun getToken(name: String, password: String): Single<ResponseBody>
    fun getRoutes(token: String, params: TripsRequestModel): Single<TripsResponse>
    fun searchPlace(chars: String): Single<SearchResponse>
    fun getPopular(): Single<PopularResponse>
    fun searchPlaceByLocation(latitude: Double, longitude: Double): Single<SearchResponse>
}