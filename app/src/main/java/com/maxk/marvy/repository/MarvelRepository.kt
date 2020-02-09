package com.maxk.marvy.repository

import com.maxk.marvy.api.marvel.MarvelService
import com.maxk.marvy.model.marvel.Character
import com.maxk.marvy.model.marvel.DataWrapper

class MarvelRepository(private val marvelService: MarvelService) {
    suspend fun searchCharacters(searchTerm: String): DataWrapper<Character> =
        marvelService.searchCharactersAsync(searchTerm)
}