package com.maxk.marvy.model

import com.squareup.moshi.Json

data class CharacterInfo(
    val biography: CharacterBiography,
    val appearance: CharacterAppearance,
    val image: CharacterImage
)

data class CharacterBiography(
    @Json(name = "full-name") val fullName: String,
    @Json(name = "alignment") val alignment: String
)

data class CharacterAppearance(
    val gender: String,
    val race: String,
    val height: List<String>,
    val weight: List<String>
)

data class CharacterImage(val url: String?)