package com.movista.app.presentation.base

import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

abstract class BaseNavActivity : BaseActivity() {

    override fun onResume() {
        super.onResume()
        getNavigationHolder().setNavigator(getNavigator())
    }

    override fun onPause() {
        getNavigationHolder().removeNavigator()
        super.onPause()
    }

    protected abstract fun getNavigationHolder(): NavigatorHolder

    protected abstract fun getNavigator(): Navigator

    protected abstract fun createNavigator(): Navigator
}