package com.movista.app.data.source.remote

import com.google.gson.JsonObject
import com.movista.app.data.entity.PopularResponse
import com.movista.app.data.entity.SearchResponse
import com.movista.app.data.entity.TripsRequestModel
import com.movista.app.data.entity.TripsResponse
import io.reactivex.Single
import okhttp3.ResponseBody

class RemoteDataSource(private val endpoint: ApiEndpoints) : IRemoteDataSource {

    override fun sendDeviceID(jo: JsonObject): Single<ResponseBody> {

        return endpoint.registerUser(jo)
    }

    override fun getToken(name: String, password: String): Single<ResponseBody> {

        return endpoint.getToken(name = name, password = password)
    }

    override fun getRoutes(token: String, params: TripsRequestModel): Single<TripsResponse> {

        return endpoint.getRoutes(params)
    }

    override fun searchPlace(chars: String): Single<SearchResponse> {

        return endpoint.searchByName(chars)
    }

    override fun getPopular(): Single<PopularResponse> {

        return endpoint.getPopular()
    }

    override fun searchPlaceByLocation(latitude: Double, longitude: Double): Single<SearchResponse> {

        return endpoint.searchByLocation(latitude, longitude)
    }
}