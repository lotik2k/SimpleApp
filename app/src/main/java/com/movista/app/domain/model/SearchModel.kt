package com.movista.app.domain.model

import com.movista.app.presentation.common.ComfortType
import java.util.*

data class SearchModel(

        var fromCity: PlaceModel? = null,
        var toCity: PlaceModel? = null,
        var isTrainSelected: Boolean = false,
        var isBusSelected: Boolean = false,
        var isPlainSelected: Boolean = false,
        var fromDate: Date? = null,
        var toDate: Date? = null,
        var passengers: List<PassengerModel> = arrayListOf(),
        var comfortType: ComfortType = ComfortType.ECONOMY
)