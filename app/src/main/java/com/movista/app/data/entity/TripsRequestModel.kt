package com.movista.app.data.entity

import com.squareup.moshi.Json

/**
 * Дата класс, сериализуемый и отправляемый
 * как параметр для получения списка маршрутов.
 * Оперирует классами из файла SearchParams
 */

data class TripsRequestModel(

        @Json(name = "params")
        val params: Params,

        @Json(name = "paging")
        val paging: Paging,

        @Json(name = "filters")
        val filters: Filters
)
