package com.movista.app.presentation.searchparams.searchparams

data class SearchParamsViewModel(
        val fromCity: String,
        val toCity: String,
        val dateLabel: String,
        val date: String,
        val passengers: String,
        val isTrainSelected: Boolean,
        val isBusSelected: Boolean,
        val isPlainSelected: Boolean
)