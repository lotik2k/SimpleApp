package com.movista.app.domain.model

import com.movista.app.presentation.common.ComfortType

data class PassengersInfoModel(
        val passengers: ArrayList<PassengerModel>,
        var comfortType: ComfortType,
        var maxCountReached: Boolean,
        var isMinAdultCount: Boolean,
        var isMinChildCount: Boolean
)