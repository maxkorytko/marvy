package com.maxk.marvy.repository

import com.maxk.marvy.api.marvel.MarvelService
import com.maxk.marvy.model.MarvelCharacter
import com.maxk.marvy.model.MarvelDataWrapper

class MarvelRepository(private val marvelService: MarvelService) {
    suspend fun fetchCharacters(): MarvelDataWrapper<MarvelCharacter> = marvelService.charactersAsync()
}