package com.movista.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.movista.android.config.PROD_URL
import com.movista.app.di.component.AppComponent
import com.movista.app.di.component.DaggerAppComponent
import com.movista.app.di.module.AppModule
import com.movista.app.di.module.RetrofitModule
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class App : Application() {

    companion object {

        const val APP_PREFERENCES = "APP_PREFERENCES"

        @JvmStatic
        lateinit var appComponent: AppComponent

        private lateinit var app: App

        fun isOnline(): Boolean {
            val connectivityManager =
                    app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return (networkInfo != null && networkInfo.isConnected)
        }

    }

    override fun onCreate() {
        super.onCreate()

        app = this

        initDagger()
        initLogging()
        initLeakCanary()
    }


    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this, APP_PREFERENCES))
                .retrofitModule(RetrofitModule(PROD_URL))
                .build()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)
    }
}