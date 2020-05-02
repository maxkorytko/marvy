package com.maxk.marvy.characters.view

import android.content.Context
import android.widget.LinearLayout
import com.maxk.marvy.R
import com.maxk.marvy.databinding.MarvelCharacterInfoViewBinding
import com.maxk.marvy.extensions.layoutInflater
import com.maxk.marvy.model.CharacterInfo
import com.maxk.marvy.model.CharacterPowerStats

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
        setupPowerStatsSection()
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

    private fun setupPowerStatsSection() = characterInfo.powerStats?.let { powerStats ->
        val powerStatsViews = arrayOf(
            CharacterPowerStats::power,
            CharacterPowerStats::strength,
            CharacterPowerStats::combat,
            CharacterPowerStats::durability,
            CharacterPowerStats::intelligence,
            CharacterPowerStats::speed
        ).mapNotNull { property ->
            property.getter.call(powerStats)?.toIntOrNull()?.let {
                Pair(property.name.capitalize(), it)
            }
        }.map(::buildPowerStatView)

        if (powerStatsViews.isNotEmpty()) {
            val powerStatsSection = MarvelCharacterInfoSectionView(context).apply {
                title = resources.getString(R.string.marvel_character_power_stats_section_title)

                powerStatsViews.forEach {
                    addRow(it, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
                }
            }

            addView(powerStatsSection, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
    }

    private fun buildPowerStatView(powerStat: Pair<String, Int>): MarvelCharacterPowerStatView {
        return MarvelCharacterPowerStatView(context).apply {
            title = powerStat.first
            stat = powerStat.second
        }
    }
}