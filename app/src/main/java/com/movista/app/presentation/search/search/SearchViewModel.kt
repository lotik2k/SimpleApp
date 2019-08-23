package com.movista.app.presentation.search.search

data class SearchViewModel(
        val fromCity: String,
        val toCity: String,
        val dateLabel: String,
        val date: String,
        val passengers: String,
        val isTrainSelected: Boolean,
        val isBusSelected: Boolean,
        val isPlainSelected: Boolean
)