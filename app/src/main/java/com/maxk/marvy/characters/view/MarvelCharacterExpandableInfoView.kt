package com.maxk.marvy.characters.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.maxk.marvy.databinding.MarvelCharacterExpandableInfoViewBinding
import com.maxk.marvy.extensions.layoutInflater
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

            expandImage.setOnContextClickListener {
                true
            }
        }
    }
}