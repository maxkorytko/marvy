package com.maxk.marvy.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridRecyclerView @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyle: Int = 0)
    : RecyclerView(context, attrs, defStyle) {

    override fun attachLayoutAnimationParameters(
        child: View?,
        params: ViewGroup.LayoutParams?,
        index: Int,
        count: Int
    ) {
        if (adapter != null && layoutManager is GridLayoutManager) {
            var animationParams =
                params?.layoutAnimationParameters as? GridLayoutAnimationController.AnimationParameters

            if (animationParams == null) {
                // If there are no animation parameters, create new ones and attach them to
                // the LayoutParams.
                animationParams = GridLayoutAnimationController.AnimationParameters()
                params?.layoutAnimationParameters = animationParams
            }

            // Next we are updating the parameters

            // Set the number of items in the RecyclerView and the index of this item
            // Next we are updating the parameters
            // Set the number of items in the RecyclerView and the index of this item
            animationParams.count = count
            animationParams.index = index

            // Calculate the number of columns and rows in the grid
            val columns = (layoutManager as GridLayoutManager).spanCount
            animationParams.columnsCount = columns
            animationParams.rowsCount = count / columns

            // Calculate the column/row position in the grid
            // Calculate the column/row position in the grid
            val invertedIndex = count - 1 - index
            animationParams.column = columns - 1 - invertedIndex % columns
            animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
        }

        super.attachLayoutAnimationParameters(child, params, index, count)
    }
}