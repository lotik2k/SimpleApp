package com.movista.app.presentation.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.View

abstract class SearchResultCustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // todo
    /*fun Int.toScaledWidth(width: Int): Float {
        // чтобы выглядело как на макете при любых размерах экрана
        // 360 - ширина макета
        return (resources.displayMetrics.widthPixels * ((this.toFloat() - 16.toFloat()) / 360.toFloat())).also { println(it) }
    }*/
}