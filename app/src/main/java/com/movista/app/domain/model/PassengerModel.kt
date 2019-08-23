package com.movista.app.domain.model

abstract class PassengerModel(
        open var age: Int?
)

data class AdultPassengerModel(val adultAge: Int = 17) : PassengerModel(adultAge)

data class ChildPassengerModel(
        override var age: Int? = null,
        var isSeatRequired: Boolean? = null
) : PassengerModel(age)
