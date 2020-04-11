package com.maxk.marvy.characters.view

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.maxk.marvy.R
import com.maxk.marvy.databinding.MarvelCharacterInfoViewBinding
import com.maxk.marvy.extensions.layoutInflater
import com.maxk.marvy.model.CharacterInfo

class MarvelCharacterInfoView(context: Context, private val characterInfo: CharacterInfo)
    : ConstraintLayout(context) {

    private val binding =
        MarvelCharacterInfoViewBinding.inflate(layoutInflater, this, true)

    init {
        setupBioSection()
    }

    private fun setupBioSection() = with(characterInfo) {
        binding.bioSection.addTextRow(R.string.marvel_character_info_name, name)
    }
}