package com.movista.app.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.movista.app.data.repository.main.IMainRepository
import com.movista.app.presentation.base.BasePresenter
import com.movista.app.presentation.common.Screens
import ru.terrakok.cicerone.Router

@InjectViewState
class MainPresenter(
        private val router: Router,
        private val repository: IMainRepository
) : BasePresenter<MainView>() {

    fun start() {

        if (repository.isFirstStart()) {
            router.newRootScreen(Screens.WELCOME)
        } else if (!repository.tokenExists()) {
            /*repository.authorizeUser()*/ // Получить токен
        } else {
            router.newRootScreen(Screens.SEARCH)
        }
    }

}