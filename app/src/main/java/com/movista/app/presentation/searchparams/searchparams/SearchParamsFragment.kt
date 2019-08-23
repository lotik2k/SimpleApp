package com.movista.app.presentation.searchparams.searchparams

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.movista.app.App
import com.movista.app.R
import com.movista.app.presentation.base.BaseFragment
import com.movista.app.utils.isVisible
import com.movista.app.utils.setGone
import com.movista.app.utils.setVisible
import com.movista.app.utils.toPx
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject
import kotlin.math.roundToInt


class SearchParamsFragment : BaseFragment(), SearchParamsView, OnMapReadyCallback {

    companion object {
        const val STUB = 0
        const val MAP_PADDING = 0
        const val TAG = "map_fragment"

        fun newInstance(): SearchParamsFragment = SearchParamsFragment()
    }

    private var mapFragment: SupportMapFragment? = null

    private var map: GoogleMap? = null
    private var fromMarker: Marker? = null
    private var toMarker: Marker? = null

    private lateinit var outerCircle: ImageView
    private lateinit var innerCircle: ImageView

    private var polyline: Polyline? = null

    @Inject
    @InjectPresenter
    lateinit var paramsPresenter: SearchParamsPresenter

    @ProvidePresenter
    fun createPresenter(): SearchParamsPresenter {
        return paramsPresenter
    }

    override fun getLayoutRes() = STUB

    override fun onFragmentInject() {
        App.appComponent.inject(this)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)

        // https://issuetracker.google.com/issues/66402372 все равно не решает проблему
        if (savedInstanceState != null) {
            mapFragment = childFragmentManager.findFragmentByTag(TAG) as SupportMapFragment
        } else {
            val options = GoogleMapOptions()
            options.liteMode(true)

            mapFragment = SupportMapFragment.newInstance(options)

            // дл] поддержки api 28
            val mapFrag: SupportMapFragment? = mapFragment

            if (mapFrag != null) {
                childFragmentManager.beginTransaction()
                        .addToBackStack(TAG)
                        .add(R.id.map_container, mapFrag, TAG)
                        .commit()
                mapFragment?.getMapAsync(this)
            }
        }
        return root
    }

    override fun initUI() {
        super.initUI()

        val parent = activity as AppCompatActivity?

        parent?.setSupportActionBar(toolbar)
        parent?.supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
            title = ""
        }

        from_text.setOnClickListener { paramsPresenter.onFromClicked() }
        to_text.setOnClickListener { paramsPresenter.onToClicked() }
        there_text.setOnClickListener { paramsPresenter.onDateClicked() }

        from_text.setOnClickListener { paramsPresenter.onFromClicked() }
        to_text.setOnClickListener { paramsPresenter.onToClicked() }

        passengers_text.setOnClickListener { paramsPresenter.onPassengersClicked() }

        find_button.setOnClickListener { paramsPresenter.onFindTicketsButtonClicked() }


        /*   outerCircle = ImageView(activity)
           innerCircle = ImageView(activity)*/
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return
        map = googleMap
        onMapReady()
    }

    override fun showFromCityName(name: String) {
        from_text.text = name
    }

    override fun showToCityName(name: String) {
        to_text.text = name
    }

    override fun showFromCityMarker(lat: Double, lon: Double) {
        val latLngFrom = LatLng(lat, lon)

        fromMarker?.remove()

        fromMarker = map?.addMarker(
                MarkerOptions()
                        .position(latLngFrom)
                        .icon(getMarkerIconFromDrawable(resources.getDrawable(R.drawable.map_place_icon)))
                        .anchor(0.5f, 0.5f)
        )

//        startAnimation(latLngFrom)
    }

    override fun showToCityMarker(lat: Double, lon: Double) {

        val latLngTo = LatLng(lat, lon)

        toMarker?.remove()

        toMarker = map?.addMarker(
                MarkerOptions()
                        .position(latLngTo)
                        .icon(getMarkerIconFromDrawable(resources.getDrawable(R.drawable.map_place_icon)))
                        .anchor(0.5f, 0.5f)
        )
    }

    override fun showPath(latFrom: Double, lonFrom: Double, latTo: Double, lonTo: Double) {
        val latLngFrom = LatLng(latFrom, lonFrom)
        val latLngTo = LatLng(latTo, lonTo)

        val latLngBounds = LatLngBounds.builder()
                .include(latLngFrom)
                .include(latLngTo)
                .build()

        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(
                latLngBounds,
                MAP_PADDING
        )

        map?.run {

            polyline?.remove()

            polyline = addPolyline(
                    PolylineOptions()
                            .add(latLngFrom, latLngTo)
                            .width(4f)
                            .color(ContextCompat.getColor(activity!!, R.color.map_line_color))
            )


            this.animateCamera(cameraUpdate)
            this.moveCamera(CameraUpdateFactory.zoomOut())
        }
    }

    override fun moveCameraToCity(lat: Double, lon: Double) {
        map?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lon)))
    }

    override fun showDate(date: String) {
        there_text.text = date
    }

    override fun changeDateLabel(label: String) {
        date_label.text = label
    }

    override fun trainSwitchOnIf(condition: Boolean) {

        switch_train.isChecked = condition
    }

    override fun plainSwitchOnIf(condition: Boolean) {

        switch_plain.isChecked = condition
    }

    override fun busSwitchOnIf(condition: Boolean) {

        switch_bus.isChecked = condition
    }

    override fun showPassengersInfo(info: String) {

        passengers_text.text = info
    }

    override fun showMapOverlay() {

        map_overlay.setVisible()

        map?.setOnMapLoadedCallback {
            map_overlay.setGone()
        }
    }

    override fun showError(error: String) {

        if (banner_search_params_error.isVisible()) {
            banner_search_params_error.setGone()
        }

        // todo добавить анимацию
        app_bar.elevation = resources.getDimension(R.dimen.app_bar_elevation)

        banner_search_params_error.setVisible()
        banner_search_params_error_text.text = error

        banner_search_params_error_dismiss.setOnClickListener {
            app_bar.elevation = 0f
            banner_search_params_error.setGone()
        }
    }

    private fun onMapReady() {
        showMapOverlay()
        map?.run {

            setOnMapClickListener { /*иначе по клику на логотип открывается приложение гугл карт*/ }

            setMapStyle(
                    MapStyleOptions(resources.getString(R.string.map_style))
            )

            uiSettings.isMapToolbarEnabled = false
        }
    }

    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    // наработки по анимации
    private fun startAnimation(latLngFrom: LatLng) {

        val screenPosition = map?.projection?.toScreenLocation(latLngFrom)

        screenPosition ?: return

        outerCircle.setImageResource(R.drawable.map_from_decoration_circle_outer)
        outerCircle.alpha = 0.2f

        innerCircle = ImageView(this.context)
        innerCircle.setImageResource(R.drawable.map_from_decoration_circle_inner)
        innerCircle.alpha = 0.4f

        val outerParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        outerParams.leftMargin = screenPosition.x - 32.toPx().roundToInt()
        outerParams.topMargin = screenPosition.y - 32.toPx().roundToInt()

        map_container.addView(outerCircle, outerParams)

        val innerParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        innerParams.leftMargin = screenPosition.x - 16.toPx().roundToInt()
        innerParams.topMargin = screenPosition.y - 16.toPx().roundToInt()

        map_container.addView(innerCircle, innerParams)

        val innerAnimationX = ObjectAnimator.ofFloat(innerCircle, "scaleX", 1.5f)
        val innerAnimationY = ObjectAnimator.ofFloat(innerCircle, "scaleY", 1.5f)
        innerAnimationX.duration = 1000
        innerAnimationX.repeatCount = ObjectAnimator.INFINITE
        innerAnimationY.duration = 1000
        innerAnimationY.repeatCount = ObjectAnimator.INFINITE
        innerAnimationX.start()
        innerAnimationY.start()

        val outerAnimationX = ObjectAnimator.ofFloat(outerCircle, "scaleX", 1.5f)
        val outerAnimationY = ObjectAnimator.ofFloat(outerCircle, "scaleY", 1.5f)
        outerAnimationX.duration = 1000
        outerAnimationX.repeatCount = ObjectAnimator.INFINITE
        outerAnimationY.duration = 1000
        outerAnimationY.repeatCount = ObjectAnimator.INFINITE
        outerAnimationX.start()
        outerAnimationY.start()
    }
}

