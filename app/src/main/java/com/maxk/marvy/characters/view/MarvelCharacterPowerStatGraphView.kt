package com.maxk.marvy.characters.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.maxk.marvy.R
import com.maxk.marvy.extensions.dp

class MarvelCharacterPowerStatGraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private data class Segment(val size: Float, val color: Int)

    private companion object {
        const val SEGMENTS_COUNT = 4
        val SPACE_BETWEEN_SEGMENTS = 4.dp

        val COLORS = arrayOf(
            R.color.powerStat25,
            R.color.powerStat50,
            R.color.powerStat75,
            R.color.powerStat100
        )

        fun segmentColor(context: Context, segmentNumber: Int): Int = ContextCompat.getColor(
            context, COLORS[segmentNumber - 1])
    }

    private val segments = ArrayList<Segment>()

    private var segmentWidth: Float = 0f

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    /**
     * Power stat value from 0 to 100
     */
    var powerStat: Int = 0
        set(value) {
            if (!(0..100).contains(value)) {
                throw IllegalArgumentException("Power stat must be between 0 and 100")
            }
            field = value
            calculateSegmentsToDraw()
            invalidate()
        }

    private fun calculateSegmentsToDraw() {
        segments.clear()

        val segmentFullSize = 100 / SEGMENTS_COUNT

        for (i in 1..4) {
            val color = segmentColor(context, i)

            if (powerStat <= i * segmentFullSize) {
                val segmentSize = (powerStat - (i - 1f) * segmentFullSize) / segmentFullSize
                segments.add(Segment(segmentSize, color))
                break
            }

            segments.add(Segment(1f, color))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val padding = paddingStart + paddingEnd
        val internalSpacing = SPACE_BETWEEN_SEGMENTS * (SEGMENTS_COUNT - 1f)

        segmentWidth = (w - (padding + internalSpacing)) / SEGMENTS_COUNT
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        segments.forEachIndexed { index, segment ->
            val space = if (index == 0) 0 else SPACE_BETWEEN_SEGMENTS

            canvas?.translate(paddingStart.toFloat(), 0f)

            canvas?.save()
            canvas?.translate(index * (segmentWidth + space), 0f)
            drawSegment(segment, canvas)
            canvas?.restore()
        }
    }

    private fun drawSegment(segment: Segment, canvas: Canvas?) = canvas?.apply {
        paint.color = segment.color

        canvas.drawRoundRect(
            RectF(0f, 0f, segmentWidth * segment.size, height.toFloat()),
            height / 2f,
            height / 2f,
            paint
        )
    }
}