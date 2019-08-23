package com.movista.app.presentation.base

import com.arellomobile.mvp.MvpView

interface LoadingView : MvpView {

    fun showLoading()
    fun hideLoading()
}