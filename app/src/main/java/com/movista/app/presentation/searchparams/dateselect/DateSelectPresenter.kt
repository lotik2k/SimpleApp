package com.movista.app.presentation.searchparams.dateselect

import android.annotation.SuppressLint
import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.movista.app.R
import com.movista.app.domain.searchparams.IDateSelectUseCase
import com.movista.app.presentation.base.BasePresenter
import com.movista.app.presentation.common.DateFormat.DAY_MONTH
import com.movista.app.presentation.common.DateFormat.MONTH_YEAR
import com.movista.app.presentation.common.DateFormat.SERVER
import com.movista.app.presentation.common.Locale.RU_LOCALE_LOW
import com.movista.app.presentation.common.Locale.RU_LOCALE_UP
import com.prolificinteractive.materialcalendarview.CalendarDay
import ru.terrakok.cicerone.Router
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

@InjectViewState
class DateSelectPresenter(
        private val router: Router,
        private val resources: Resources,
        private val useCase: IDateSelectUseCase
) : BasePresenter<DateSelectView>() {

    private var thereDate: Date? = null
    private var returnDate: Date? = null
    private var oneDateClickedCount = 0

    init {

        viewState.setThereText(
                resources.getString(
                        R.string.date_select_from_code,
                        SimpleDateFormat(DAY_MONTH, Locale(RU_LOCALE_LOW, RU_LOCALE_UP))
                                .format(useCase.getToday())
                )
        )

        viewState.setReturnText(resources.getString(R.string.date_select_to))

        viewState.addOneDateDecorator(CalendarDay.today())
        viewState.applyViewState(DateSelectViewState.THERE_DATE_SELECTED)
    }

    override fun attachView(view: DateSelectView?) {
        super.attachView(view)

        viewState.setCalendarMinDate(Calendar.getInstance().time)
        viewState.setTitleFormatter(prepareDateFormat())
    }

    fun onDateClicked(date: CalendarDay) {

        val resultDateFormats = formatDates(date)
        thereDate = resultDateFormats.second

        viewState.removeDecorators()

        fun whenOnlyThereSelected() {

            returnDate = null

            viewState.setThereText(
                    resources.getString(R.string.date_select_from_code, resultDateFormats.first)
            )

            viewState.setReturnText(
                    resources.getString(R.string.date_select_to)
            )

            viewState.addOneDateDecorator(date)
            viewState.applyViewState(DateSelectViewState.THERE_DATE_SELECTED)
        }

        fun whenThereAndReturnSelected() {

            returnDate = thereDate

            viewState.addOneDateAsThereAndReturnDecorator(date)

            viewState.setThereText(
                    resources.getString(R.string.date_select_from_code, resultDateFormats.first)
            )

            viewState.setReturnText(
                    resultDateFormats.first
            )

            viewState.applyViewState(DateSelectViewState.ONE_DATE_FOR_THERE_AND_RETURN_DATE_SELECTED)
        }

        if (oneDateClickedCount < 2) oneDateClickedCount++ else oneDateClickedCount = 1

        when (oneDateClickedCount) {

            1 -> whenOnlyThereSelected()
            2 -> whenThereAndReturnSelected()
        }

        newDateSelected()
    }

    fun onRangeSelected(dates: List<CalendarDay>) {

        oneDateClickedCount = 0

        viewState.removeDecorators()
        viewState.addDecorators(dates)

        val firstDate = formatDates(dates[0])
        val lastDate = formatDates(dates[dates.size - 1])

        thereDate = firstDate.second
        returnDate = lastDate.second

        viewState.setThereText(
                resources.getString(R.string.date_select_from_code, firstDate.first)
        )
        viewState.setReturnText(lastDate.first)

        viewState.applyViewState(DateSelectViewState.THERE_AND_RETURN_DATE_SELECTED)

        newDateSelected()
    }

    fun onNoReturnButtonClicked() {
        router.exit()
    }

    fun onDoneButtonClicked() {
        router.exit()
    }

    private fun formatDates(date: CalendarDay): Pair<String, Date> {
        val calendar = Calendar.getInstance()
        date.copyTo(calendar)
        val dateFormatted: String = SimpleDateFormat(DAY_MONTH, Locale(RU_LOCALE_LOW, RU_LOCALE_UP))
                .format(calendar.time)
        val serverDateFormatted: Date = SimpleDateFormat(SERVER, Locale.ROOT)
                .parse(calendar.time.toString())
        return Pair(dateFormatted, serverDateFormatted)
    }

    @SuppressLint("SimpleDateFormat") // предоставляем собственные значения
    private fun prepareDateFormat(): DateFormat {

        /* приводим к виду "Май/Июнь/...", с большой буквы и в именительном падеже" */
        val months = resources.getStringArray(R.array.months)
        val dateFormatSymbols = DateFormatSymbols()
        dateFormatSymbols.months = months

        return SimpleDateFormat(MONTH_YEAR, dateFormatSymbols)
    }

    private fun newDateSelected() {

        val thereDate = thereDate
        val returnDate = returnDate

        thereDate ?: return

        useCase.setTripDate(thereDate, returnDate)
    }
}