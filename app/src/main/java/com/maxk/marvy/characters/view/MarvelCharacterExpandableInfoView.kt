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
            name.text = characterInfo.biography.fullName

            infoContainer.addView(infoView, LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            ))
        }

        fetchCharacterImage()
    }

    private fun fetchCharacterImage() {
        Glide.with(context)
            .load(characterInfo.image.url)
            .into(binding.image)
    }
}