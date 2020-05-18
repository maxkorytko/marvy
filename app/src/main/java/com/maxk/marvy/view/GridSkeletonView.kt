package com.maxk.marvy.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.maxk.marvy.R

class GridSkeletonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private val space: Int = context.resources.getDimension(R.dimen.space).toInt()

    private val blocks: MutableList<View> = ArrayList()

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_VERTICAL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        blocks.forEach {
            removeView(it)
        }

        blocks.clear()

        createBlocks(w, h).forEach {
            addView(it)
            blocks.add(it)
        }

        post { requestLayout() }
    }

    private fun createBlocks(parentWidth: Int, parentHeight: Int): List<View> {
        // Assuming that the grid has two columns, there is a margin on both sides of each column.
        var blockSize = (parentWidth / 2) - (space * 4)
        var blocksCount = parentHeight / blockSize
        // There is a margin on both sides of the block.
        val spaceAvailable = parentHeight - (blocksCount * space * 2)
        blocksCount = spaceAvailable / blockSize

        // Reduce the leftover space if possible.
        val leftOverSpace = parentHeight - (blocksCount * blockSize)
        if (leftOverSpace > blockSize / 2) {
            blocksCount++
            blockSize = spaceAvailable / blocksCount
        }

        return (1..blocksCount).map { createBlock(blockSize) }
    }

    private fun createBlock(size: Int): View {
        val container = LinearLayout(context)

        val containerLayoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        container.layoutParams = containerLayoutParams
        container.orientation = HORIZONTAL
        container.gravity = Gravity.CENTER

        (1..2).forEach {
            val layoutParams = LayoutParams(size, size)
            layoutParams.setMargins(space, space, space, space)

            val block = View(context)
            block.background = ColorDrawable(resources.getColor(R.color.colorSurface, context.theme))
            block.layoutParams = layoutParams
            container.addView(block)
        }

        return container
    }
}