package com.movista.app.presentation.searchparams.passengersinfo

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface PassengersInfoView : MvpView {

    fun showPassengersInfo(passengersInfoViewModel: PassengersInfoViewModel)

    fun disableIncreaseAdultCountIf(condition: Boolean)
    fun disableIncreaseChildrenCountIf(condition: Boolean)
    fun disableDecreaseAdultCountIf(condition: Boolean)
    fun disableDecreaseChildrenCountIf(condition: Boolean)

    fun deleteChildInfoButton(id: Int)

    fun navigateBack()

    @StateStrategyType(SkipStrategy::class)
    fun showChooseComfortTypeDialog(comfortTypes: Array<String>, selected: Int)

    @StateStrategyType(SkipStrategy::class)
    fun showChildInfoDialog(
            id: Int,
            childInfo: ChildInfo,
            minAge: Int,
            maxAge: Int,
            disableIsSeatRequired: Pair<Boolean, Boolean>
    )

    @StateStrategyType(SkipStrategy::class)
    fun showAgeCategoryInfoDialog()

    @StateStrategyType(SkipStrategy::class)
    fun showError(error: String)
}