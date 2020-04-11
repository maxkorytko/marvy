package com.maxk.marvy.model.superhero

import com.maxk.marvy.model.CharacterInfo

data class SearchResponse(
    val error: String?,
    val results: List<CharacterInfo>?
)