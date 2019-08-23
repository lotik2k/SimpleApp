package com.movista.app.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.movista.app.presentation.view.common.SearchResultCustomView
import com.movista.app.utils.toPx

class TripItemView(context: Context, attrs: AttributeSet) : SearchResultCustomView(context, attrs) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val transferPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    //    private val dy = resources.getDimension(R.dimen.е6).toPxx()
    private val dy = 6.toPx()
    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        linePaint.color = Color.parseColor("#49577F")
        linePaint.style = Paint.Style.FILL
        linePaint.strokeWidth = 12.toPx()
        linePaint.strokeCap = Paint.Cap.ROUND

        canvas.translate(16.toPx(), 0f)

        val timelineWidth = width - (16.toPx() + 5.toPx())
        val offset = 24f / 31f

        canvas.drawLine(offset / timelineWidth, 0f, (offset / timelineWidth) + 60f, 0f, linePaint)
    }

    private fun Int.toScaledWidth(): Float {
        // чтобы выглядело как на макете при любых размерах экрана
        // 360 - ширина макета
        return (resources.displayMetrics.widthPixels * ((this.toFloat()) / 360.toFloat()))
    }

    private fun toTime(hours: Int, minutes: Int): Float {
        if (minutes < 0) return hours.toFloat()

        val minutesPercentage = minutes.toFloat() / 60.toFloat()
        return hours + minutesPercentage
    }
}