package com.movista.app.utils

import android.content.res.Resources
import android.view.View
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.roundToInt

fun View.setGone() {
    visibility = View.GONE
}

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInVisible() {
    visibility = View.INVISIBLE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun <T> Single<T>.schedulersIoToMain(): Single<T> {
    return subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread())
}

inline val String.Companion.EMPTY: String
    get() = ""

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).roundToInt()

fun Int.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Int.spToPx(): Float = this * Resources.getSystem().displayMetrics.scaledDensity

/*
fun Context.getScreenWidth(): Int {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}*/
