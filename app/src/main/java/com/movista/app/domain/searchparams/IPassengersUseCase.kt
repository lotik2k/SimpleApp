package com.movista.app.domain.searchparams

import com.movista.app.domain.model.PassengersInfoModel
import io.reactivex.subjects.PublishSubject

interface IPassengersUseCase {

    fun requestPassengersModel()
    fun setPassengersInfo()
    fun increaseAdultCount()
    fun decreaseAdultCount()
    fun increaseChildrenCount()
    fun decreaseChildrenCount()
    fun observePassengersInfoParamsUpdate(): PublishSubject<PassengersInfoModel>
    fun notifyNewPassengersInfoConfirmed()
    fun updateChildInfo(index: Int, age: Int, isSeatRequired: Boolean)
}