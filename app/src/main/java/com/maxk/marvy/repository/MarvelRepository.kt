package com.maxk.marvy.repository

import com.maxk.marvy.api.marvel.MarvelService
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.model.marvel.DataWrapper

class MarvelRepository(private val marvelService: MarvelService) {
    suspend fun searchCharacters(searchTerm: String): DataWrapper<MarvelCharacter> =
        marvelService.searchCharactersAsync(searchTerm)
}