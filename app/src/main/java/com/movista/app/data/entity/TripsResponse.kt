package com.movista.app.data.entity

import com.squareup.moshi.Json

/**
 * Дата класс, десериализуемый из ответа с сервера,
 * отправляющего список маршрутов
 * Оперирует классами из файла SearchParams
 */

data class TripsResponse(

        @Json(name = "isError")
        val isError: Boolean, // false

        @Json(name = "data")
        val data: TripsData
)

data class TripsData(

        @Json(name = "uid")
        val uid: String, // 22e3a469e2894257a037a61af8c4607d

        @Json(name = "routes")
        val routes: List<Route>,

        @Json(name = "trips")
        val trips: List<Trip>,

        @Json(name = "filters")
        val filters: List<Filter>,

        @Json(name = "services")
        val services: List<Service>,

        @Json(name = "placeResponses")
        val placeResponses: List<PlaceResponseModel>
)
