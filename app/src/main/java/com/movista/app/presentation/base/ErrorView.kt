package com.movista.app.presentation.base

import com.arellomobile.mvp.MvpView

interface ErrorView : MvpView {

    fun showNetError()
    fun showError(error: String)
}