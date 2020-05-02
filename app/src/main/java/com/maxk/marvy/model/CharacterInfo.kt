package com.maxk.marvy.model

import com.squareup.moshi.Json

data class CharacterInfo(
    val biography: CharacterBiography,
    val appearance: CharacterAppearance,
    val image: CharacterImage,
    @Json(name = "powerstats") val powerStats: CharacterPowerStats?
)

data class CharacterBiography(
    @Json(name = "full-name") val fullName: String,
    val alignment: String
)

data class CharacterAppearance(
    val gender: String,
    val race: String,
    val height: List<String>,
    val weight: List<String>
)

data class CharacterPowerStats(
    val intelligence: String,
    val strength: String,
    val speed: String,
    val durability: String,
    val power: String,
    val combat: String
)

data class CharacterImage(val url: String?)