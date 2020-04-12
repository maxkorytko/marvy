package com.maxk.marvy.characters.view

import android.content.Context
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.maxk.marvy.R
import com.maxk.marvy.databinding.MarvelCharacterInfoViewBinding
import com.maxk.marvy.extensions.layoutInflater
import com.maxk.marvy.model.CharacterInfo

class MarvelCharacterInfoView(context: Context, private val characterInfo: CharacterInfo)
    : LinearLayout(context) {

    private val binding =
        MarvelCharacterInfoViewBinding.inflate(layoutInflater, this, true)

    init {
        orientation = VERTICAL
        dividerDrawable = resources.getDrawable(R.drawable.spacer, context.theme)
        showDividers = SHOW_DIVIDER_MIDDLE

        setupBioSection()
        setupAppearanceSection()
    }

    private fun setupBioSection() = with(binding.bioSection) {
        with(characterInfo.biography) {
            addTextRow(R.string.marvel_character_info_name, fullName)
            addTextRow(R.string.marvel_character_info_alignment, alignment)
        }
    }

    private fun setupAppearanceSection() = with(binding.appearanceSection) {
        with(characterInfo.appearance) {
            addTextRow(R.string.marvel_character_info_gender, gender)
            addTextRow(R.string.marvel_character_info_race, race)
            addTextRow(R.string.marvel_character_info_height, height.joinToString(", "))
            addTextRow(R.string.marvel_character_info_weight, weight.joinToString(", "))
        }
    }
}