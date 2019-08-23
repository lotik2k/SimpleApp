package com.movista.app.data.entity

import com.squareup.moshi.Json

data class SearchResponse(

        @Json(name = "isError")
        val isError: Boolean,

        @Json(name = "data")
        val data: SearchData
)

data class SearchData(

        @Json(name = "places")
        val places: List<PlaceResponseModel>
)
