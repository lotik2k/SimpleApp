package com.movista.app.data.converter

import com.movista.app.data.entity.TripsResponse
import com.movista.app.presentation.viewmodel.RouteViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.ROOT

class RoutesConverter {

    companion object {
        const val FULL_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
        const val IN_DURATION_PATTERN = "HH:mm:ss"
        const val OUT_DURATION_PATTERN = "HH:mm"
    }

    private val sdfFullDate = SimpleDateFormat(FULL_DATE_PATTERN, ROOT)

    fun toRouteModel(tripsResponse: TripsResponse): List<RouteViewModel> {

        val routes = arrayListOf<RouteViewModel>()

        tripsResponse.data.trips.forEachIndexed { index, trip ->
            // to parse incoming trip duration
            val sdfInDuration = SimpleDateFormat(IN_DURATION_PATTERN, ROOT)
            // to format incoming date/duration
            val sdfOut = SimpleDateFormat(OUT_DURATION_PATTERN, ROOT)

            with(trip) {
                val route = RouteViewModel(
                        validatingCarrierName,
                        direction,
                        sdfOut.format(sdfFullDate.parse(departure)),
                        sdfOut.format(sdfFullDate.parse(arrival)),
                        tripsResponse.data.services[index].price.toInt().toString(),
                        sdfOut.format(sdfInDuration.parse(duration))
                )
                routes.add(route)
            }
        }
        return routes
    }

    fun getNextDayDate(): String {

        val date = GregorianCalendar.getInstance()
        val day = date.get(Calendar.DAY_OF_MONTH)
        date.set(Calendar.DAY_OF_MONTH, day + 1) // set to the next day
        return sdfFullDate.format(date.time)
    }
}