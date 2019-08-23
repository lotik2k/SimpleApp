package com.movista.app.domain.searchparams

import java.util.*

interface IDateSelectUseCase {

    fun setTripDate(from: Date, to: Date?)
    fun getToday(): Date
}