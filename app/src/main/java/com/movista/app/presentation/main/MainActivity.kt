package com.movista.app.presentation.main

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.movista.app.App
import com.movista.app.R
import com.movista.app.R.layout.ac_main
import com.movista.app.presentation.base.BaseNavActivity
import com.movista.app.presentation.common.Screens
import com.movista.app.presentation.onboarding.OnboardingActivity
import com.movista.app.presentation.searchparams.dateselect.DateSelectActivity
import com.movista.app.presentation.searchparams.passengersinfo.PassengersInfoActivity
import com.movista.app.presentation.searchparams.placesearch.PlaceSearchActivity
import com.movista.app.presentation.searchparams.searchparams.SearchParamsFragment
import com.movista.app.presentation.searchresult.loading.LoadingTicketsFragment
import com.movista.app.presentation.searchresult.searchresult.SearchResultActivity
import kotlinx.android.synthetic.main.ac_main.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import javax.inject.Inject

class MainActivity : BaseNavActivity(), MainView {

    @Inject
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder

    private val navigator = createNavigator()

    override fun getLayoutRes() = ac_main

    override fun onActivityInject() {
        App.appComponent.inject(this)
    }

    override fun afterCreate() {
        super.afterCreate()

        presenter.start()
    }

    override fun getNavigationHolder() = navigatorHolder

    override fun getNavigator() = navigator

    override fun createNavigator(): Navigator {

        return object : SupportAppNavigator(this, R.id.container_view) {

            override fun createActivityIntent(
                    context: Context?,
                    screenKey: String?,
                    data: Any?
            ): Intent? {
                return when (screenKey) {
                    Screens.WELCOME -> Intent(
                            this@MainActivity,
                            OnboardingActivity::class.java
                    )
                    Screens.PLACE_SEARCH -> PlaceSearchActivity.start(
                            this@MainActivity,
                            data
                    )
                    Screens.DATE_SELECT -> Intent(
                            this@MainActivity,
                            DateSelectActivity::class.java
                    )
                    Screens.PASSENGERS_INFO -> Intent(
                            this@MainActivity,
                            PassengersInfoActivity::class.java
                    )

                    Screens.SEARCH_RESULT -> Intent(
                            this@MainActivity,
                            SearchResultActivity::class.java
                    )
                    else -> null
                }
            }

            override fun createFragment(screenKey: String?, data: Any?): Fragment {
                return when (screenKey) {
                    Screens.SEARCH -> SearchParamsFragment.newInstance()
                    Screens.LOADING_TICKETS_SEARCH_RESULT -> {
                        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                        LoadingTicketsFragment.newInstance()
                    }
                    else -> SearchParamsFragment.newInstance()
                }
            }


            /*override fun setupFragmentTransactionAnimation(
                    command: Command?,
                    currentFragment: Fragment?,
                    nextFragment: Fragment?,
                    fragmentTransaction: FragmentTransaction?
            ) {
                if (command is Forward && currentFragment is SearchParamsFragment && nextFragment is LoadingTicketsFragment) {
                    fragmentTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                }
            }*/
        }
    }

    @ProvidePresenter
    fun createPresenter(): MainPresenter {
        return presenter
    }
}