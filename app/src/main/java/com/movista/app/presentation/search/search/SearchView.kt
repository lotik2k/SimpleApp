package com.movista.app.presentation.search.search

import com.arellomobile.mvp.MvpView

interface SearchView : MvpView {

    fun showFromCityName(name: String)
    fun showToCityName(name: String)
    fun showFromCityMarker(lat: Double, lon: Double)
    fun showToCityMarker(lat: Double, lon: Double)
    fun showMapOverlay()
    fun showPath(latFrom: Double, lonFrom: Double, latTo: Double, lonTo: Double)
    fun moveCameraToCity(lat: Double, lon: Double)
    fun showDate(date: String)
    fun trainSwitchOnIf(condition: Boolean)
    fun plainSwitchOnIf(condition: Boolean)
    fun busSwitchOnIf(condition: Boolean)
    fun changeDateLabel(label: String)
    fun showPassengersInfo(info: String)
}