package com.movista.app.presentation.search.search

import com.arellomobile.mvp.InjectViewState
import com.movista.app.domain.model.SearchModel
import com.movista.app.domain.search.ISearchUseCase
import com.movista.app.presentation.base.BasePresenter
import com.movista.app.presentation.common.Screens
import com.movista.app.presentation.converter.SearchConverter
import com.movista.app.presentation.search.placesearch.PlaceSearchType
import com.movista.app.utils.EMPTY
import ru.terrakok.cicerone.Router

@InjectViewState
class SearchPresenter(
        private val router: Router,
        private val searchUseCase: ISearchUseCase,
        private val converter: SearchConverter
) : BasePresenter<SearchView>() {

    init {

        onNewPlaceReceived(searchUseCase.initSearchParams())
    }


    override fun attachView(view: SearchView?) {
        super.attachView(view)


        addDisposable(
                searchUseCase.observeSearchParamsUpdate()
                        .subscribe {
                            onNewPlaceReceived(it)
                        }

        )
    }

    fun onFromClicked() {
        router.navigateTo(Screens.PLACE_SEARCH, PlaceSearchType.FROM)
    }

    fun onToClicked() {
        router.navigateTo(Screens.PLACE_SEARCH, PlaceSearchType.TO)
    }

    fun onDateClicked() {
        router.navigateTo(Screens.DATE_SELECT)
    }

    fun onPassengersClicked() {
        router.navigateTo(Screens.PASSENGERS_INFO)
    }

    private fun onNewPlaceReceived(searchModel: SearchModel) {

        applyToView(converter.toSearchViewModel(searchModel))

        viewState.showMapOverlay()

        with(searchModel) {

            val from = fromCity
            val to = toCity

            when {
                from != null && to != null -> {
                    viewState.showPath(from.lat, from.lon, to.lat, to.lon)
                    viewState.showFromCityMarker(from.lat, from.lon)
                    viewState.showToCityMarker(to.lat, to.lon)
                }

                from != null -> {
                    viewState.showFromCityMarker(from.lat, from.lon)
                    viewState.moveCameraToCity(from.lat, from.lon)
                }

                to != null -> {
                    viewState.showToCityMarker(to.lat, to.lon)
                    viewState.moveCameraToCity(to.lat, to.lon)
                }

            }
        }
    }

    private fun applyToView(searchViewModel: SearchViewModel) {

        with(searchViewModel) {
            if (fromCity != kotlin.String.EMPTY) viewState.showFromCityName(fromCity)
            if (toCity != kotlin.String.EMPTY) viewState.showToCityName(toCity)
            // todo тумблеры
            viewState.showDate(date)
            viewState.showPassengersInfo(passengers)
            viewState.changeDateLabel(dateLabel)
            viewState.busSwitchOnIf(isBusSelected)
            viewState.trainSwitchOnIf(isTrainSelected)
            viewState.plainSwitchOnIf(isPlainSelected)
        }
    }
}