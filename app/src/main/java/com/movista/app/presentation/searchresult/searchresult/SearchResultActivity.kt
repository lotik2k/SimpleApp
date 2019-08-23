package com.movista.app.presentation.searchresult.searchresult

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.movista.app.App
import com.movista.app.R
import com.movista.app.presentation.base.BaseNavActivity
import com.movista.app.presentation.common.Screens
import com.movista.app.presentation.searchresult.loading.LoadingTicketsFragment
import com.movista.app.presentation.searchresult.tickets.TicketsSearchResultFragment
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import javax.inject.Inject

class SearchResultActivity : BaseNavActivity(), SearchResultView {

    private val navigator = createNavigator()

    @InjectPresenter
    lateinit var presenter: SearchResultPresenter

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder

    override fun getLayoutRes() = R.layout.activity_search_result

    override fun onActivityInject() {
        App.appComponent.inject(this)
    }

    override fun getNavigationHolder() = navigatorHolder

    override fun getNavigator() = navigator

    override fun createNavigator(): Navigator {

        return object : SupportAppNavigator(this, R.id.search_result_container) {

            override fun createActivityIntent(
                    context: Context?,
                    screenKey: String?,
                    data: Any?
            ): Intent? {
                return when (screenKey) {
                    else -> null
                }
            }

            override fun createFragment(screenKey: String?, data: Any?): Fragment {
                return when (screenKey) {
                    Screens.LOADING_TICKETS_SEARCH_RESULT -> LoadingTicketsFragment.newInstance()
                    else -> TicketsSearchResultFragment.newInstance()
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
}
