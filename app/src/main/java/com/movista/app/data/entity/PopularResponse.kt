package com.movista.app.data.entity

import com.squareup.moshi.Json

data class PopularResponse(

        @Json(name = "isError")
        val isError: Boolean, // false

        @Json(name = "data")
        val data: PopularData
)

data class PopularData(

        @Json(name = "placesPopular")
        val placesPopular: List<PlaceResponseModel>,

        @Json(name = "placesRecent")
        val placesRecent: List<PlaceResponseModel>
)