package com.movista.app.presentation.searchresult.searchresult

import android.os.Handler
import com.arellomobile.mvp.InjectViewState
import com.movista.app.App
import com.movista.app.presentation.base.BasePresenter
import com.movista.app.presentation.common.Screens
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class SearchResultPresenter : BasePresenter<SearchResultView>() {

    @Inject
    lateinit var router: Router

    init {
        App.appComponent.inject(this)

        router.newRootScreen(Screens.LOADING_TICKETS_SEARCH_RESULT)

        Handler().postDelayed(
                {
                    router.newRootScreen(Screens.SEARCH_RESULT)
                },
                3000
        )
    }
}