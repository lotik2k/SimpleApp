package com.movista.app.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.movista.app.R
import com.movista.app.utils.spToPx
import com.movista.app.utils.toPx

class TimelineView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var firstDate: String? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        setBackgroundColor(ContextCompat.getColor(this.context, android.R.color.transparent))

        drawLines(canvas)
        drawText(canvas)
    }

    private fun drawLines(canvas: Canvas) {
        canvas.save()

        linePaint.color = Color.parseColor("#3A486F")
        linePaint.strokeWidth = resources.getDimension(R.dimen.size_xxxsmall)
        linePaint.strokeCap = Paint.Cap.ROUND

        canvas.translate(16.toPx(), 32.toPx())

        for (i in 0..31) {
            val xz = i.toFloat() / 31.toFloat()
            val xCoord = (width - 21.toPx()) * xz
            val dyStart = 32.toPx()
            val dyEnd = 48.toPx()

            // todo comment
            when (i) {
                0 -> {
                    canvas.drawLine((xCoord), 0f, (xCoord), 16.toPx(), linePaint)
                    canvas.drawLine(xCoord, dyStart, xCoord, dyEnd, linePaint)
                }
                6, 12, 18, 30 -> canvas.drawLine((xCoord), 0f, xCoord, 16.toPx(), linePaint)
                24 -> {
                    canvas.drawLine((xCoord), 0f, xCoord, 16.toPx(), linePaint)
                    canvas.drawLine(xCoord, dyStart, xCoord, dyEnd, linePaint)
                }
                31 -> canvas.drawLine((xCoord), 10.toPx(), (xCoord), 16.toPx(), linePaint)
                else -> canvas.drawLine(xCoord, 10.toPx(), xCoord, 16.toPx(), linePaint)
            }
        }

        canvas.translate(0f, 24.toPx())

        linePaint.strokeWidth = 4.toPx()
        linePaint.color = Color.parseColor("#1E2A4C")

        canvas.drawLine(0f, 0f, resources.displayMetrics.widthPixels.toFloat(), 0f, linePaint)

        canvas.restore()
    }

    private fun drawText(canvas: Canvas) {

        fun dxComplete(n: Int) = ((width - 21.toPx()) * (n.toFloat() / 31.toFloat()))

        canvas.save()

        textPaint.textSize = 10.spToPx()
        textPaint.color = Color.WHITE
        textPaint.textAlign = Paint.Align.CENTER // draw from the center

        val rect = Rect()
        textPaint.getTextBounds("00:00", 0, "00.00".length, rect)

        canvas.translate(8.toPx(), (rect.height() + 12.toPx()))

        canvas.drawText("0:00", 0f, 0f, textPaint)
        canvas.drawText("6:00", dxComplete(6), 0f, textPaint)
        canvas.drawText("12:00", dxComplete(12), 0f, textPaint)
        canvas.drawText("18:00", dxComplete(18), 0f, textPaint)
        canvas.drawText("0:00", dxComplete(24), 0f, textPaint)
        canvas.drawText("6:00", dxComplete(30), 0f, textPaint)

        canvas.restore()

        canvas.save()
        canvas.translate(16.toPx(), 76.toPx())

        val xEndFirst = (width - 21.toPx()) * (24f / 31f)

        val xFirst = xEndFirst / 2f

        val rectDate = Rect()
        textPaint.getTextBounds("data", 0, "data".length, rectDate)
        textPaint.color = Color.parseColor("#3A486F")
        canvas.drawText("data", xFirst - (rectDate.width() / 2f), 0f, textPaint)

        val xEndStartSecond = xEndFirst
        val xEndSecond = width
        val xSecond = width - ((xEndSecond - xEndStartSecond) / 2f)

        canvas.drawText("data", xSecond - (rectDate.width() / 2f), 0f, textPaint)


        if (firstDate != null) {
            canvas.save()
            textPaint.getTextBounds(firstDate, 0, firstDate!!.length, rect)
            canvas.translate(147.toScaledWidthLines() - (rect.width() / 2), 72.toPx())
            textPaint.color = Color.parseColor("#3A486F")
            canvas.drawText(firstDate ?: "null", 0f, 0f, textPaint)
            canvas.restore()
        }
    }

    private fun Int.toScaledWidthLines(): Float {
        // чтобы выглядело как на макете при любых размерах экрана
        // 360 - ширина макета
        // 16 - компенсация смещения по x (canvas.translate(16.toPx(), 32.toPx()))
        return (resources.displayMetrics.widthPixels * ((this.toFloat() - 16.toFloat()) / 360.toFloat()))
    }

    private fun Int.toScaledWidthText(): Float {
        // чтобы выглядело как на макете при любых размерах экрана
        // 360 - ширина макета
        return (resources.displayMetrics.widthPixels * ((this.toFloat() - 4.toFloat()) / 360.toFloat()))
    }
}