package com.movista.app.presentation.converter

import android.content.res.Resources
import com.movista.app.R
import com.movista.app.data.entity.PlaceResponseModel
import com.movista.app.data.entity.PopularResponse
import com.movista.app.data.entity.SearchResponse
import com.movista.app.domain.model.AdultPassengerModel
import com.movista.app.domain.model.ChildPassengerModel
import com.movista.app.domain.model.PassengersInfoModel
import com.movista.app.domain.model.SearchModel
import com.movista.app.presentation.common.ComfortType
import com.movista.app.presentation.common.DateFormat.DAY_MONTH
import com.movista.app.presentation.common.Locale.RU_LOCALE_LOW
import com.movista.app.presentation.common.Locale.RU_LOCALE_UP
import com.movista.app.presentation.searchparams.passengersinfo.ChildInfo
import com.movista.app.presentation.searchparams.passengersinfo.PassengersInfoViewModel
import com.movista.app.presentation.searchparams.searchparams.SearchParamsViewModel
import com.movista.app.presentation.viewmodel.PlaceViewModel
import com.movista.app.presentation.viewmodel.PopularDataViewModel
import com.movista.app.utils.EMPTY
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchConverter(private val resources: Resources) {

    fun toSearchViewModel(searchModel: SearchModel): SearchParamsViewModel {

        val sdf = SimpleDateFormat(DAY_MONTH, Locale(RU_LOCALE_LOW, RU_LOCALE_UP))
        val dateString = if (searchModel.toDate == null) {
            sdf.format(searchModel.fromDate)
        } else {
            resources.getString(
                    R.string.fg_search_date_full,
                    sdf.format(searchModel.fromDate),
                    sdf.format(searchModel.toDate)
            )
        }

        val adults = searchModel.passengers.filter { it is AdultPassengerModel }
        val children = searchModel.passengers.filter { it is ChildPassengerModel }

        val adultsLabel = if (adults.isNotEmpty()) {
            resources.getQuantityString(R.plurals.passengers_info_adults, adults.size, adults.size)
        } else {
            resources.getQuantityString(R.plurals.passengers_info_adults, 0, 0)
        }

        val childrenLabel = if (children.isNotEmpty()) {
            resources.getQuantityString(
                    R.plurals.passengers_info_children,
                    children.size,
                    children.size
            )
        } else {
            String.EMPTY
        }

        val comfortTypeLabel = toComfortTypeString(searchModel.comfortType)

        with(searchModel) {
            return SearchParamsViewModel(
                    fromCity = fromCity?.cityName ?: String.EMPTY,
                    toCity = toCity?.cityName ?: String.EMPTY,

                    dateLabel = if (searchModel.toDate == null) {
                        resources.getString(R.string.fg_search_there_title)
                    } else {
                        resources.getString(R.string.fg_search_there_and_return_title)
                    },

                    date = dateString,

                    // todo пофиксить запятые
                    passengers = "$adultsLabel, $childrenLabel, ${comfortTypeLabel.toLowerCase()}",

                    isTrainSelected = isTrainSelected,
                    isBusSelected = isBusSelected,
                    isPlainSelected = isPlainSelected
            )
        }
    }

    fun toPlaceViewModelList(searchResponse: SearchResponse): List<PlaceViewModel> {

        val places = arrayListOf<PlaceViewModel>()
        searchResponse.data.places.forEach {
            places.add(
                    toViewModel(it)
            )
        }
        return places
    }

    fun toPlaceViewModel(searchByLocationResponse: SearchResponse): PlaceViewModel {

        //первый город в списке - самый подходящий по координатам
        val placeResponse = searchByLocationResponse.data.places[0]
        return toViewModel(placeResponse)
    }

    fun toPopularViewModel(popularResponse: PopularResponse): PopularDataViewModel {
        val listPopular = arrayListOf<PlaceViewModel>()
        val listRecent = arrayListOf<PlaceViewModel>()

        popularResponse.data.placesPopular.forEach {
            listPopular.add(toViewModel(it))
        }

        popularResponse.data.placesRecent.forEach {
            listRecent.add(toViewModel(it))
        }

        return PopularDataViewModel(listPopular, listRecent)
    }

    fun toPassengersInfoViewModel(passengersInfo: PassengersInfoModel, minAge: Int): PassengersInfoViewModel {

        with(passengersInfo) {

            val adults = passengers.filter { it is AdultPassengerModel }
            val children = passengers.filter { it is ChildPassengerModel }
            val childrenInfo = ArrayList<ChildInfo>()

            if (children.isNotEmpty()) {
                children.forEach {
                    val child = it as ChildPassengerModel
                    val infoEdited: Boolean

                    val age = child.age
                    val ageLabel = if (age == null) {
                        infoEdited = false
                        String.EMPTY
                    } else {
                        infoEdited = true
                        if (age == 0) {
                            resources.getString(R.string.passenger_child_age_zero)
                        } else {
                            resources.getQuantityString(R.plurals.passengers_child_age, age, age)
                        }
                    }

                    val isSeatRequired = child.isSeatRequired

                    val seatLabel = if (isSeatRequired == null) {
                        String.EMPTY
                    } else {
                        if (isSeatRequired) {
                            resources.getString(R.string.passengers_with_seat)
                        } else {
                            resources.getString(R.string.passengers_without_seat)
                        }
                    }

                    childrenInfo.add(
                            ChildInfo(
                                    child.age ?: minAge,
                                    child.isSeatRequired ?: false,
                                    infoEdited,
                                    ageLabel,
                                    seatLabel
                            )
                    )
                }
            }

            val comfortTypeString = toComfortTypeString(comfortType)

            return PassengersInfoViewModel(
                    passengersInfo.comfortType,
                    adults.size,
                    children.size,
                    childrenInfo,
                    comfortTypeString
            )
        }
    }

    private fun toViewModel(placeResponseModel: PlaceResponseModel): PlaceViewModel {

        with(placeResponseModel) {
            return PlaceViewModel(
                    cityName,
                    stateName ?: String.EMPTY,
                    countryName,
                    lat,
                    lon
            )
        }
    }

    private fun toComfortTypeString(comfortType: ComfortType): String {

        return when (comfortType) {
            ComfortType.ECONOMY -> resources.getString(R.string.comfort_type_economy)
            ComfortType.PREMIUM_ECONOMY -> resources.getString(R.string.comfort_type_premium_economy)
            ComfortType.BUSINESS -> resources.getString(R.string.comfort_type_business)
            ComfortType.FIRST_CLASS -> resources.getString(R.string.comfort_type_first_class)
        }
    }
}