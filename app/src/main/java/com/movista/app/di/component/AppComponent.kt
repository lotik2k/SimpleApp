package com.movista.app.di.component

import com.movista.app.di.module.AppModule
import com.movista.app.di.module.MainModule
import com.movista.app.di.module.RetrofitModule
import com.movista.app.di.module.SearchParamsModule
import com.movista.app.presentation.main.MainActivity
import com.movista.app.presentation.onboarding.OnboardingActivity
import com.movista.app.presentation.searchparams.dateselect.DateSelectActivity
import com.movista.app.presentation.searchparams.passengersinfo.PassengersInfoActivity
import com.movista.app.presentation.searchparams.passengersinfo.PassengersInfoPresenter
import com.movista.app.presentation.searchparams.placesearch.PlaceSearchActivity
import com.movista.app.presentation.searchparams.searchparams.SearchParamsFragment
import com.movista.app.presentation.searchresult.searchresult.SearchResultActivity
import com.movista.app.presentation.searchresult.searchresult.SearchResultPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = arrayOf(
                AppModule::class,
                RetrofitModule::class,
                MainModule::class,
                SearchParamsModule::class
        )
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(onboardingActivity: OnboardingActivity)
    fun inject(placeSearchActivity: PlaceSearchActivity)
    fun inject(dateSelectActivity: DateSelectActivity)
    fun inject(searchFragment: SearchParamsFragment)
    fun inject(passengersInfoActivity: PassengersInfoActivity)
    fun inject(passengersInfoPresenter: PassengersInfoPresenter)
    fun inject(searchResultActivity: SearchResultActivity)
    fun inject(searchResultPresenter: SearchResultPresenter)
}