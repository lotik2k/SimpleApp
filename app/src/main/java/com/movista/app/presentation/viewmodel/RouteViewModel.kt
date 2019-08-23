package com.movista.app.presentation.viewmodel

data class RouteViewModel(
        val carrierName: String,
        val direction: String,
        val departure: String,
        val arrival: String,
        val price: String,
        val total: String
)