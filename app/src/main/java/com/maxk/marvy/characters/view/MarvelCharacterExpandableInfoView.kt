package com.maxk.marvy.characters.view

import android.content.Context
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.maxk.marvy.databinding.MarvelCharacterExpandableInfoViewBinding
import com.maxk.marvy.extensions.getValueAnimator
import com.maxk.marvy.extensions.layoutInflater
import com.maxk.marvy.model.CharacterInfo

class MarvelCharacterExpandableInfoView(context: Context, private val characterInfo: CharacterInfo)
    : LinearLayout(context) {

    private val binding =
        MarvelCharacterExpandableInfoViewBinding.inflate(layoutInflater, this, true)

    private val infoView = MarvelCharacterInfoView(context, characterInfo)

    private var expandedHeight: Int = -1

    init {
        orientation = VERTICAL

        with(binding) {
            model = characterInfo

            infoContainer.addView(
                infoView,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )

            headerContainer.setOnClickListener { expandOrCollapseCharacterInfo() }
            expandImage.setOnClickListener { expandOrCollapseCharacterInfo() }
        }
    }

    private fun expandOrCollapseCharacterInfo() {
        setExpanded(binding.infoContainer.height == 0)
    }

    private fun setExpanded(expanded: Boolean) {
        val animator = getValueAnimator(
            forward = expanded,
            duration = resources.getInteger(android.R.integer.config_mediumAnimTime),
            interpolator = DecelerateInterpolator()
        ) { progress ->
            with(binding) {
                infoContainer.updateLayoutParams { height = (expandedHeight * progress).toInt() }
                expandImage.rotation = 180 * progress
            }
        }

        animator.start()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (expandedHeight < 0) {
            // Ensure this code runs exactly once.
            expandedHeight = 0
            binding.infoContainer.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            expandedHeight = binding.infoContainer.measuredHeight
        }
    }
}
