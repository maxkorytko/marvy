package com.maxk.marvy.repository

import com.maxk.marvy.api.marvel.MarvelService
import com.maxk.marvy.model.marvel.Character
import com.maxk.marvy.model.marvel.DataWrapper

class MarvelRepository(private val marvelService: MarvelService) {
    suspend fun fetchCharacters(): DataWrapper<Character> = marvelService.charactersAsync()
}