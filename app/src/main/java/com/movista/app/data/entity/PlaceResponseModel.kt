package com.movista.app.data.entity

import com.squareup.moshi.Json

data class PlaceResponseModel(

        @Json(name = "id")
        val id: Int, // 83130

        @Json(name = "lon")
        val lon: Double, // 30.314130783081055

        @Json(name = "lat")
        val lat: Double, // 59.938629150390625

        @Json(name = "countryName")
        val countryName: String, // Россия

        @Json(name = "stateName")
        val stateName: String?, // Ленинградская обл.

        @Json(name = "cityName")
        val cityName: String, // Санкт-Петербург

        @Json(name = "timeZone")
        val timeZone: String, // Asia/Oral

        @Json(name = "stationName")
        val stationName: String, // а/п Домодедово

        @Json(name = "platformName")
        val platformName: String, // а/п Домодедово

        @Json(name = "description")
        val description: String // Россия
)