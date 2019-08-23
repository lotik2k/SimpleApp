package com.movista.app.domain.searchparams

import com.movista.app.presentation.common.ComfortType

interface IComfortTypeUseCase {

    fun setComfortType(comfortType: ComfortType)
}