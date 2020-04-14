package com.maxk.marvy.characters.view

import android.animation.LayoutTransition
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.maxk.marvy.databinding.MarvelCharacterExpandableInfoViewBinding
import com.maxk.marvy.extensions.layoutInflater
import com.maxk.marvy.extensions.resolveAttribute
import com.maxk.marvy.model.CharacterInfo

class MarvelCharacterExpandableInfoView(context: Context, private val characterInfo: CharacterInfo)
    : ConstraintLayout(context) {

    private val binding =
        MarvelCharacterExpandableInfoViewBinding.inflate(layoutInflater, this, true)

    private val infoView = MarvelCharacterInfoView(context, characterInfo)

    init {
        with(binding) {
            model = characterInfo

            infoContainer.addView(infoView, LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            ))

            val layoutTransition = LayoutTransition()
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            infoContainer.layoutTransition = layoutTransition

            expandImage.setOnClickListener {
                setExpanded(binding.infoContainer.height == 0)
                true
            }
        }
    }

    private fun setExpanded(expanded: Boolean) = binding.infoContainer.updateLayoutParams {
        height = if (expanded) LayoutParams.WRAP_CONTENT else 0
        binding.expandImage.animate().rotationBy(-180f)
    }
}
