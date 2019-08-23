package com.movista.app.presentation.searchparams.searchparams

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.movista.app.domain.model.SearchModel
import com.movista.app.domain.searchparams.ISearchParamsUseCase
import com.movista.app.presentation.base.BasePresenter
import com.movista.app.presentation.common.Screens
import com.movista.app.presentation.converter.SearchConverter
import com.movista.app.presentation.searchparams.placesearch.PlaceSearchType
import com.movista.app.utils.EMPTY
import ru.terrakok.cicerone.Router

@InjectViewState
class SearchParamsPresenter(
        private val router: Router,
        private val searchUseCase: ISearchParamsUseCase,
        private val converter: SearchConverter,
        private val resources: Resources
) : BasePresenter<SearchParamsView>() {

    private lateinit var searchParamsViewModel: SearchParamsViewModel

    init {
        onNewPlaceReceived(searchUseCase.initSearchParams())
    }

    override fun attachView(view: SearchParamsView?) {
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

    fun onFindTicketsButtonClicked() {
        /*if (searchParamsViewModel.fromCity == String.EMPTY && searchParamsViewModel.toCity == String.EMPTY) {
            viewState.showError(resources.getString(R.string.error_search_params_no_from_to_city))
        } else if (searchParamsViewModel.toCity == String.EMPTY) {
            viewState.showError(resources.getString(R.string.error_search_params_no_to_city))
        } else {
            // move to the next screen
            router.navigateTo(Screens.LOADING_TICKETS_SEARCH_RESULT)
        }*/
        router.newScreenChain(Screens.SEARCH_RESULT)
    }

    private fun onNewPlaceReceived(searchModel: SearchModel) {

        searchParamsViewModel = converter.toSearchViewModel(searchModel)

        applyToView()

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

    private fun applyToView() {

        with(searchParamsViewModel) {
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