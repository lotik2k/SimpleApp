package com.movista.app.presentation.searchparams.dateselect

import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.movista.app.presentation.base.ErrorView
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.DateFormat
import java.util.*

interface DateSelectView : ErrorView {

    fun setCalendarMinDate(date: Date)
    fun setTitleFormatter(dateFormat: DateFormat)
    fun setThereText(text: String)
    fun setReturnText(text: String)
    fun applyViewState(dateSelectViewState: DateSelectViewState)

    @StateStrategyType(SingleStateStrategy::class)
    fun addDecorators(dates: List<CalendarDay>)

    fun removeDecorators()
    fun addOneDateDecorator(date: CalendarDay)

    @StateStrategyType(SingleStateStrategy::class)
    fun addOneDateAsThereAndReturnDecorator(day: CalendarDay)
}