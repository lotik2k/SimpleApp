package com.movista.app.presentation.searchparams.placesearch

import com.movista.app.presentation.base.ErrorView
import com.movista.app.presentation.base.LoadingView
import com.movista.app.presentation.viewmodel.PlaceViewModel

interface PlaceSearchView : LoadingView, ErrorView {

    fun setHint(from: String)
    fun showMyLocation(place: PlaceViewModel)
    fun checkLocationPermissionIsGranted()
    fun requestLocation()
    fun requestLocationPermission()
    fun setSearchAdapter(adapter: PlaceSearchAdapter)
    fun setPopularAdapter(adapter: PlaceSearchFromListAdapter)
    fun setRecentAdapter(adapter: PlaceSearchFromListAdapter)
    fun applyViewState(placeSearchViewState: PlaceSearchViewState)
}