package com.movista.app.presentation.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.movista.app.presentation.common.Error
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        Timber.i("${this.javaClass.simpleName} destroy")
        super.onDestroy()
        clearDisposable()
    }

    // call only this fun when work with rx
    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun isNetError(error: Throwable): Boolean {
        return error.message == Error.NO_CONNECTION
    }

    fun clearDisposable() = compositeDisposable.clear()
}