package com.maxk.marvy.characters.view

import android.animation.LayoutTransition
import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import com.maxk.marvy.databinding.MarvelCharacterExpandableInfoViewBinding
import com.maxk.marvy.extensions.layoutInflater
import com.maxk.marvy.model.CharacterInfo

class MarvelCharacterExpandableInfoView(context: Context, private val characterInfo: CharacterInfo)
    : LinearLayout(context) {

    private val binding =
        MarvelCharacterExpandableInfoViewBinding.inflate(layoutInflater, this, true)

    private val infoView = MarvelCharacterInfoView(context, characterInfo)

    init {
        orientation = VERTICAL
        setupInfoContainer()

        with(binding) {
            model = characterInfo

            headerContainer.setOnClickListener { expandOrCollapseCharacterInfo() }
            expandImage.setOnClickListener { expandOrCollapseCharacterInfo() }
        }
    }

    private fun setupInfoContainer() = with(binding.infoContainer) {
        addView(infoView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun expandOrCollapseCharacterInfo() {
        setExpanded(binding.infoContainer.height == 0)
    }

    private fun setExpanded(expanded: Boolean) = binding.infoContainer.updateLayoutParams {
        height = if (expanded) LayoutParams.WRAP_CONTENT else 0
        binding.expandImage.animate().rotationBy(-180f)
    }
}
