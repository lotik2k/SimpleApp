package com.movista.app.presentation.searchparams.placesearch

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.movista.app.R
import com.movista.app.data.entity.PopularResponse
import com.movista.app.domain.searchparams.IPlaceSearchUseCase
import com.movista.app.presentation.base.BasePresenter
import com.movista.app.presentation.common.Error
import com.movista.app.presentation.converter.SearchConverter
import com.movista.app.presentation.searchparams.placesearch.PlaceSearchType.FROM
import com.movista.app.presentation.searchparams.placesearch.PlaceSearchType.TO
import com.movista.app.presentation.viewmodel.PlaceViewModel
import com.movista.app.utils.EMPTY
import com.movista.app.utils.schedulersIoToMain
import ru.terrakok.cicerone.Router
import timber.log.Timber
import java.io.Serializable

@InjectViewState
class PlaceSearchPresenter(
        private val router: Router,
        private val useCase: IPlaceSearchUseCase,
        private val converter: SearchConverter,
        private val resources: Resources
) :
        BasePresenter<PlaceSearchView>(),
        PlaceSearchAdapter.OnPlaceSearchItemListener,
        PlaceSearchFromListAdapter.OnRecentPlacesItemListener {

    private var placeSearchType: PlaceSearchType? = null

    private lateinit var myLocationCity: PlaceViewModel

    private lateinit var placeSearchAdapter: PlaceSearchAdapter
    private lateinit var recentAdapter: PlaceSearchFromListAdapter
    private lateinit var popularListAdapter: PlaceSearchFromListAdapter

    private var lacationExists = false
    private var popularExists = false
    private var locationIsGranted = false
    private var recentExists = false


    override fun onDestroy() {
        super.onDestroy()
        placeSearchAdapter.removeListener()
        recentAdapter.removeListener()
        popularListAdapter.removeListener()
    }

    override fun onPlaceItemClicked(placeViewModel: PlaceViewModel) {
        when (placeSearchType) {
            FROM -> setFromCity(placeViewModel)
            TO -> setToCity(placeViewModel)
        }
        router.exit()
    }

    override fun onRecentItemClicked(placeViewModel: PlaceViewModel) {
        when (placeSearchType) {
            FROM -> setFromCity(placeViewModel)
            TO -> setToCity(placeViewModel)
        }
        router.exit()
    }

    fun start(data: Serializable?) {

        placeSearchType = data as? PlaceSearchType
        placeSearchType?.let {
            when (it) {
                FROM -> {
                    doWhenFromScreen()
                }
                TO -> {
                    doWhenToScreen()
                }
            }
        } ?: Timber.e("param is null")
    }

    fun search(chars: String) {

        if (chars.isEmpty()) {
            doWhenEmptySearchField()
            return
        }

        clearDisposable()

        addDisposable(
                useCase.search(chars)
                        .schedulersIoToMain()
                        .doOnSubscribe {
                            viewState.applyViewState(PlaceSearchViewState.SHOW_LOADING_SEARCH_RESULT)
                        }
                        .doAfterTerminate { viewState.hideLoading() }
                        .subscribe(
                                {
                                    placeSearchAdapter.setItems(converter.toPlaceViewModelList(it))
                                    placeSearchAdapter.notifyDataSetChanged()
                                    viewState.applyViewState(PlaceSearchViewState.SHOW_SEARCH_RESULT)
                                },
                                {
                                    if (it.message == Error.NO_CONNECTION) {
                                        viewState.showNetError()
                                    } else {
                                        viewState.showError(String.EMPTY)
                                    }
                                    Timber.d("ServerError $it")
                                }
                        )
        )
    }

    fun onLocationReceived(latitude: Double, longitude: Double) {
        addDisposable(
                useCase.getPlaceInfo(latitude, longitude)
                        .schedulersIoToMain()
                        .subscribe(
                                {
                                    myLocationCity = converter.toPlaceViewModel(it)
                                    viewState.showMyLocation(myLocationCity)
                                    lacationExists = true
                                    canShowInitResult()

                                },
                                { Timber.d("ServerError $it") }
                        )
        )
    }

    fun onNullLocationReceived() {
        lacationExists = false
        if (popularExists) {
            viewState.applyViewState(PlaceSearchViewState.SHOW_ONLY_RECENT)
        } else {
            doWhenFromScreen()
        }
    }

    fun userGrandPermission(isTrue: Boolean) {
        if (isTrue) {
            viewState.applyViewState(PlaceSearchViewState.SHOW_LOADING_LOCATION)
            viewState.requestLocation()
        } else {
            // todo
        }
    }

    fun locationPermissionIsGranted(condition: Boolean) {
        if (condition) {
            if (lacationExists) canShowInitResult() else viewState.requestLocation()
        } else {
            canShowInitResult()
        }
    }

    fun onMyLocationButtonClicked() {
        viewState.requestLocationPermission()
    }

    fun onMyLocationButtonContainerClicked() {
        onPlaceClicked(myLocationCity)
    }

    private fun doWhenEmptySearchField() {
        if (placeSearchType == FROM) {
            when {
                lacationExists && popularExists ->
                    viewState.applyViewState(PlaceSearchViewState.SHOW_LOCATION_AND_RECENT)
                !lacationExists && popularExists ->
                    viewState.applyViewState(PlaceSearchViewState.SHOW_ONLY_RECENT)
                else -> doWhenFromScreen()
            }
        } else if (placeSearchType == TO) {
            if (popularExists) {
                viewState.applyViewState(PlaceSearchViewState.SHOW_RECENT_AND_POPULAR)
            } else {
                doWhenToScreen()
            }

        }
    }

    private fun onPlaceClicked(placeViewModel: PlaceViewModel) {
        placeSearchType ?: return

        when (placeSearchType) {

            FROM -> {
                with(placeViewModel) {
                    useCase.setFromCity(cityName, latitude, longitude)
                }
            }
            TO -> {
                with(placeViewModel) {
                    useCase.setToCity(cityName, latitude, longitude)
                }
            }
        }
        router.exit()
    }


    private fun setFromCity(placeViewModel: PlaceViewModel) {
        with(placeViewModel) {
            useCase.setFromCity(cityName, latitude, longitude)
        }
    }

    private fun setToCity(placeViewModel: PlaceViewModel) {
        with(placeViewModel) {
            useCase.setToCity(cityName, latitude, longitude)
        }
    }

    private fun doWhenFromScreen() {

        viewState.applyViewState(PlaceSearchViewState.SHOW_LOADING_LOCATION)

        setupAdapters()

        viewState.setHint(resources.getString(R.string.choose_departure_place))

        getPopular()

        viewState.checkLocationPermissionIsGranted()

    }

    private fun setupAdapters() {

        placeSearchAdapter = PlaceSearchAdapter(this)
        popularListAdapter = PlaceSearchFromListAdapter(this, PlaceSearchFromListType.POPULAR)
        recentAdapter = PlaceSearchFromListAdapter(this, PlaceSearchFromListType.RECENT)

        viewState.setSearchAdapter(placeSearchAdapter)
        viewState.setPopularAdapter(popularListAdapter)
        viewState.setRecentAdapter(recentAdapter)
    }

    private fun doWhenToScreen() {

        viewState.applyViewState(PlaceSearchViewState.SHOW_LOADING_LOCATION)

        setupAdapters()

        viewState.setHint(resources.getString(R.string.choose_arrival_place))

        getPopular()
    }

    private fun getRecent() {}

    private fun getPopular() {

        addDisposable(
                useCase.getPopular()
                        .schedulersIoToMain()
                        .subscribe(
                                {
                                    onPopularReceived(it)
                                },
                                { Timber.d("ServerError $it") }
                        )
        )
    }

    private fun onPopularReceived(popularResponse: PopularResponse) {

        val popularViewModel = converter.toPopularViewModel(popularResponse)

        popularListAdapter.setItems(popularViewModel.popularViewModelList)
        popularListAdapter.notifyDataSetChanged()

        recentAdapter.setItems(popularViewModel.recentViewModelList)
        recentAdapter.notifyDataSetChanged()

        popularExists = true

        when (placeSearchType) {
            FROM -> canShowInitResult()
            TO -> viewState.applyViewState(PlaceSearchViewState.SHOW_RECENT_AND_POPULAR)
        }
    }

    private fun canShowInitResult() {

        if (lacationExists && popularExists) {
            viewState.applyViewState(PlaceSearchViewState.SHOW_LOCATION_AND_RECENT)
        } else if (popularExists && !lacationExists) {
            viewState.applyViewState(PlaceSearchViewState.SHOW_ONLY_RECENT)
        }
    }


}