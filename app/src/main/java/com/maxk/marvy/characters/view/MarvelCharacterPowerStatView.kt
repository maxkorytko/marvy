package com.maxk.marvy.characters.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.updatePadding
import com.maxk.marvy.R
import com.maxk.marvy.extensions.dp

class MarvelCharacterPowerStatView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val headerView = TextRowView(context)

    private val graphView = MarvelCharacterPowerStatGraphView(context).apply {
        updatePadding(right = headerView.measureTextWidth("100"))
        updatePadding(right = paddingRight + resources.getDimensionPixelSize(R.dimen.space))
    }

    var title: CharSequence
        get() = headerView.title
        set(value) { headerView.title = value }

    var stat: Int
        get() = graphView.powerStat
        set(value) {
            headerView.text = value.toString()
            graphView.powerStat = value
        }

    init {
        orientation = VERTICAL

        addView(headerView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(graphView, LayoutParams.MATCH_PARENT, 10.dp)
    }
}