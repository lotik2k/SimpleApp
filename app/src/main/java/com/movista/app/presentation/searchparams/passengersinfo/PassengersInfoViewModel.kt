package com.movista.app.presentation.searchparams.passengersinfo

import com.movista.app.presentation.common.ComfortType

data class PassengersInfoViewModel(
        val comfortType: ComfortType,
        val adultCount: Int,
        val childrenCount: Int,
        val childrenInfo: ArrayList<ChildInfo>,
        val comfortTypeLabel: String
)

data class ChildInfo(
        val age: Int,
        val seatRequired: Boolean,
        val infoEdited: Boolean,
        val ageLabel: String,
        val seatLabel: String
)