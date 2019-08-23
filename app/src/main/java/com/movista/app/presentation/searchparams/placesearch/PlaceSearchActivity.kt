package com.movista.app.presentation.searchparams.placesearch

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.location.LocationServices
import com.movista.app.App
import com.movista.app.R
import com.movista.app.R.layout.activity_place_search
import com.movista.app.presentation.base.BaseNavActivity
import com.movista.app.presentation.searchparams.placesearch.PlaceSearchViewState.*
import com.movista.app.presentation.viewmodel.PlaceViewModel
import com.movista.app.utils.setGone
import com.movista.app.utils.setInVisible
import com.movista.app.utils.setVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_place_search.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import java.io.Serializable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PlaceSearchActivity : BaseNavActivity(), PlaceSearchView {

    companion object {

        const val INTENT_KEY = "type"
        const val PERMISSIONS_REQUEST_LOCATION = 0

        fun start(from: Context, data: Any?): Intent {

            val intent = Intent(from, PlaceSearchActivity::class.java)
            if (data is Serializable) intent.putExtra(INTENT_KEY, data)
            return intent
        }
    }

    private val navigator = createNavigator()

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var router: Router

    @Inject
    @InjectPresenter
    lateinit var presenter: PlaceSearchPresenter

    @ProvidePresenter
    fun createPresenter(): PlaceSearchPresenter {
        return presenter
    }

    @Inject
    internal lateinit var navigatorHolder: NavigatorHolder

    override fun getLayoutRes() = activity_place_search

    override fun onActivityInject() {
        App.appComponent.inject(this)
    }

    override fun initUI() {
        super.initUI()

        supportActionBar?.run {
            setHomeAsUpIndicator(R.drawable.back_button_black)
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        my_location_button.setOnClickListener {
            presenter.onMyLocationButtonClicked()
        }

        my_location_container.setOnClickListener {
            my_location_container.setBackgroundColor(
                    ContextCompat.getColor(it.context, R.color.place_search_button_clicked)
            )
            presenter.onMyLocationButtonContainerClicked()
        }

        city_list.layoutManager = LinearLayoutManager(this)
        city_list.setHasFixedSize(true)

        popular_place_list.layoutManager = LinearLayoutManager(this)
        popular_place_list.setHasFixedSize(true)

        recent_place_list.layoutManager = LinearLayoutManager(this)
        recent_place_list.setHasFixedSize(true)

        clear_text.setOnClickListener {
            city_search.text.clear()
        }
        clear_text.setGone()
    }


    override fun afterCreate() {
        super.afterCreate()

        presenter.start(intent.getSerializableExtra(INTENT_KEY)) // null if no value found
    }

    override fun getNavigationHolder() = navigatorHolder

    override fun getNavigator() = navigator

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
        addDisposable()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {

        if (
                requestCode == PERMISSIONS_REQUEST_LOCATION
                && grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            presenter.userGrandPermission(true)
        } else {
            presenter.userGrandPermission(false)
        }
    }


    override fun showLoading() {
        progress_bar_place_search.setVisible()

    }

    override fun hideLoading() {
        progress_bar_place_search.setInVisible()
    }

    override fun showNetError() {

    }

    override fun showError(error: String) {

    }

    override fun applyViewState(placeSearchViewState: PlaceSearchViewState) {

        when (placeSearchViewState) {

            SHOW_LOCATION_AND_RECENT -> {
                my_location_button.setGone()
                my_location_container.setVisible()
                content_layout_place_search_popular.setVisible()
                city_list.setGone()
                recent_place_list.setVisible()
                popular_place_list.setGone()
                progress_bar_place_search.setGone()
            }

            SHOW_ONLY_RECENT -> {
                my_location_button.setVisible()
                my_location_container.setInVisible()
                content_layout_place_search_popular.setVisible()
                city_list.setGone()
                recent_place_list.setVisible()
                popular_place_list.setGone()
                progress_bar_place_search.setGone()
            }

            SHOW_SEARCH_RESULT -> {
                my_location_button.setGone()
                my_location_container.setGone()
                content_layout_place_search_popular.setGone()
                city_list.setVisible()
                progress_bar_place_search.setGone()
            }

            SHOW_LOADING_LOCATION -> {
                progress_bar_place_search.setVisible()
                my_location_button.setGone()
                my_location_container.setGone()
                content_layout_place_search_popular.setGone()
            }

            SHOW_LOADING_SEARCH_RESULT -> {
                progress_bar_place_search.setVisible()
                my_location_button.setGone()
                my_location_container.setGone()
                content_layout_place_search_popular.setGone()
                city_list.setVisible()
            }

            SHOW_RECENT_AND_POPULAR -> {
                progress_bar_place_search.setGone()
                my_location_button.setGone()
                my_location_container.setGone()
                content_layout_place_search_popular.setVisible()
                city_list.setGone()
                recent_place_list.setVisible()
                popular_place_list.setVisible()
            }

            else -> return
        }
    }

    override fun setHint(from: String) {
        city_search.hint = from
    }

    override fun createNavigator(): Navigator {

        return object : Navigator {

            override fun applyCommands(commands: Array<Command>) {
                for (command in commands) applyCommand(command)
            }

            private fun applyCommand(command: Command) {
                if (command is Back) {
                    finish()
                }
            }

        }
    }

    override fun showMyLocation(place: PlaceViewModel) {
        my_location_city.text = place.cityName
        my_location_state.text = place.stateName

    }

    override fun checkLocationPermissionIsGranted() {

        presenter.locationPermissionIsGranted(permissionIsGranted())
    }

    @SuppressLint("MissingPermission")
    override fun requestLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    if (it != null) {
                        presenter.onLocationReceived(it.latitude, it.longitude)
                    } else {
                        presenter.onNullLocationReceived()
                    }
                }
    }

    override fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    override fun setSearchAdapter(adapter: PlaceSearchAdapter) {
        city_list.adapter = adapter
    }

    override fun setPopularAdapter(adapter: PlaceSearchFromListAdapter) {
        popular_place_list.adapter = adapter
    }

    override fun setRecentAdapter(adapter: PlaceSearchFromListAdapter) {
        recent_place_list.adapter = adapter
    }

    private fun permissionIsGranted(): Boolean {

        return ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun addDisposable() {
        compositeDisposable.add(
                createTextChangeObservable() // main
                        .debounce(350, TimeUnit.MILLISECONDS) // computation
                        .observeOn(AndroidSchedulers.mainThread()) // main
                        .subscribe { presenter.search(it) }
        )
    }


    private fun createTextChangeObservable(): Observable<String> {

        return Observable.create { emitter ->

            val watcher = object : TextWatcher {

                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null) {
                        emitter.onNext(p0.toString())

                        if (p0.isNotEmpty()) clear_text.setVisible() else clear_text.setGone()
                    }
                }
            }

            city_search.addTextChangedListener(watcher)

            emitter.setCancellable { city_search.removeTextChangedListener(watcher) }

        }
    }
}
