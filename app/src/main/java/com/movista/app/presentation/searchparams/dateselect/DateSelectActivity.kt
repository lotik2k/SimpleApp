package com.movista.app.presentation.searchparams.dateselect

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.movista.app.App
import com.movista.app.R
import com.movista.app.presentation.base.BaseNavActivity
import com.movista.app.utils.setGone
import com.movista.app.utils.setVisible
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter
import kotlinx.android.synthetic.main.activity_date_select.*
import org.jetbrains.anko.textColor
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class DateSelectActivity : BaseNavActivity(), DateSelectView {

    private val navigator = createNavigator()
    private var dateRangeMiddleDecorator: DayViewDecorator? = null
    private var dateRangeFirstDecorator: DayViewDecorator? = null
    private var dateRangeLastDecorator: DayViewDecorator? = null
    private var dateThereAndReturnDecorator: DayViewDecorator? = null
    private var oneDayDecorator: DayViewDecorator? = null

    @Inject
    lateinit var router: Router

    @Inject
    @InjectPresenter
    lateinit var presenter: DateSelectPresenter

    @ProvidePresenter
    fun createPresenter(): DateSelectPresenter {
        return presenter
    }

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder

    override fun getLayoutRes() = R.layout.activity_date_select

    override fun onActivityInject() {
        App.appComponent.inject(this)
    }

    @SuppressLint("SimpleDateFormat")
    override fun initUI() {
        super.initUI()


        back_button.setOnClickListener { super.onBackPressed() }

        calendar_view.addDecorators(boldDecorator())
        calendar_view.selectionMode = MaterialCalendarView.SELECTION_MODE_RANGE

        calendar_view.setOnDateChangedListener { widget, date, selected ->
            presenter.onDateClicked(date)
        }

        calendar_view.setOnRangeSelectedListener { widget, dates ->
            presenter.onRangeSelected(dates)
        }

        no_return_needed.setOnClickListener { presenter.onNoReturnButtonClicked() }
        date_select_done_button.setOnClickListener { presenter.onDoneButtonClicked() }
    }

    override fun getNavigationHolder() = navigatorHolder

    override fun getNavigator() = navigator

    override fun createNavigator(): Navigator {

        return object : Navigator {

            override fun applyCommands(commands: Array<Command>) {
                for (command in commands) applyCommand(command)
            }

            private fun applyCommand(command: Command) {
                if (command is Back) {
                    finish()
                }
            }

        }
    }

    override fun showNetError() {

    }

    override fun showError(error: String) {

    }

    override fun setCalendarMinDate(date: Date) {
        calendar_view.state()
                .edit()
                .setMinimumDate(date)
                .commit()
    }

    override fun setTitleFormatter(dateFormat: DateFormat) {
        val formatter = DateFormatTitleFormatter(dateFormat)
        calendar_view.setTitleFormatter(formatter)
    }

    override fun setThereText(text: String) {
        there_date.text = text
    }

    override fun setReturnText(text: String) {
        return_date.text = text
    }

    override fun applyViewState(dateSelectViewState: DateSelectViewState) {

        when (dateSelectViewState) {

            DateSelectViewState.NO_DATE_SELECTED -> {
                there_date.textColor = ContextCompat.getColor(this, R.color.text_color_grey)
                return_date.textColor = ContextCompat.getColor(this, R.color.text_color_grey)
                date_select_done_button.setGone()
            }

            DateSelectViewState.ONE_DATE_FOR_THERE_AND_RETURN_DATE_SELECTED -> {
                there_date.textColor = ContextCompat.getColor(this, R.color.text_color_black)
                return_date.textColor = ContextCompat.getColor(this, R.color.text_color_black)
                date_select_done_button.setVisible()
            }

            DateSelectViewState.THERE_DATE_SELECTED -> {
                there_date.textColor = ContextCompat.getColor(this, R.color.text_color_black)
                return_date.textColor = ContextCompat.getColor(this, R.color.text_color_grey)
                no_return_needed.setVisible()
                date_select_done_button.setGone()
            }

            DateSelectViewState.THERE_AND_RETURN_DATE_SELECTED -> {
                there_date.textColor = ContextCompat.getColor(this, R.color.text_color_black)
                return_date.textColor = ContextCompat.getColor(this, R.color.text_color_black)
                date_select_done_button.setVisible()
            }
        }
    }

    override fun addDecorators(dates: List<CalendarDay>) {
        dateRangeMiddleDecorator = dateRangeMiddleDecorator(dates)
        dateRangeFirstDecorator = dateRangeFirstDecorator(dates)
        dateRangeLastDecorator = dateRangeLastDecorator(dates)
        calendar_view.addDecorators(
                dateRangeMiddleDecorator,
                dateRangeFirstDecorator,
                dateRangeLastDecorator
        )
    }

    override fun removeDecorators() {
        calendar_view.removeDecorator(dateRangeMiddleDecorator)
        calendar_view.removeDecorator(dateRangeFirstDecorator)
        calendar_view.removeDecorator(dateRangeLastDecorator)
        calendar_view.removeDecorator(dateThereAndReturnDecorator)
        calendar_view.removeDecorator(oneDayDecorator)
    }

    override fun addOneDateDecorator(date: CalendarDay) {
        // иначе при клике даты из выбранного диапазона сбрасывается индикация
        calendar_view.selectedDate = date
        oneDayDecorator = oneDayDecorator(date)
        calendar_view.addDecorator(oneDayDecorator)
    }

    override fun addOneDateAsThereAndReturnDecorator(day: CalendarDay) {

        dateThereAndReturnDecorator = dateFromAndReturnSelected(day)
        calendar_view.addDecorator(dateThereAndReturnDecorator)
    }

    private fun boldDecorator(): DayViewDecorator {

        val calendar = GregorianCalendar.getInstance()

        return object : DayViewDecorator {

            override fun shouldDecorate(day: CalendarDay?): Boolean {

                day?.copyTo(calendar) ?: return false

                return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                        || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
            }

            override fun decorate(view: DayViewFacade?) {
                view?.addSpan(StyleSpan(Typeface.BOLD))
            }
        }
    }

    private fun oneDayDecorator(dayFrom: CalendarDay): DayViewDecorator {

        return object : DayViewDecorator {

            override fun shouldDecorate(day: CalendarDay?): Boolean {
                return dayFrom == day
            }

            override fun decorate(view: DayViewFacade?) {
                view?.setSelectionDrawable(
                        resources.getDrawable(R.drawable.date_select_calendar_selector)
                )
                view?.addSpan(ForegroundColorSpan(Color.WHITE))
            }
        }
    }

    private fun dateRangeMiddleDecorator(dates: List<CalendarDay>?): DayViewDecorator {

        return object : DayViewDecorator {

            override fun shouldDecorate(day: CalendarDay?): Boolean {
                dates ?: return false
                return day?.isInRange(dates[1], dates[dates.size - 2]) ?: false
            }

            override fun decorate(view: DayViewFacade?) {
                view?.setSelectionDrawable(
                        resources.getDrawable(R.drawable.date_select_calendar_selector_range_middle)
                )
                view?.addSpan(ForegroundColorSpan(Color.BLACK))
            }
        }
    }

    private fun dateRangeFirstDecorator(dates: List<CalendarDay>?): DayViewDecorator {

        return object : DayViewDecorator {

            override fun shouldDecorate(day: CalendarDay?): Boolean {
                dates ?: return false
                return day == dates[0]
            }

            override fun decorate(view: DayViewFacade?) {
                view?.setSelectionDrawable(
                        resources.getDrawable(R.drawable.date_select_calendar_selector_range_first)
                )
            }
        }
    }

    private fun dateRangeLastDecorator(dates: List<CalendarDay>?): DayViewDecorator {


        return object : DayViewDecorator {


            override fun shouldDecorate(day: CalendarDay?): Boolean {

                dates ?: return false

                return day == dates[dates.size - 1]


            }

            override fun decorate(view: DayViewFacade?) {
                view?.setSelectionDrawable(
                        resources.getDrawable(R.drawable.date_select_calendar_selector_range_last)
                )
            }
        }
    }

    private fun dateFromAndReturnSelected(date: CalendarDay): DayViewDecorator {


        return object : DayViewDecorator {


            override fun shouldDecorate(day: CalendarDay?): Boolean {

                return date == day
            }

            override fun decorate(view: DayViewFacade?) {
                view?.setSelectionDrawable(
                        resources.getDrawable(R.drawable.date_select_calendar_selector_both)
                )
                view?.addSpan(ForegroundColorSpan(Color.WHITE))
            }
        }
    }
}
