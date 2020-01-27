package com.maxk.marvy.api.marvel

import com.maxk.marvy.model.MarvelCharacter
import com.maxk.marvy.model.MarvelDataWrapper
import retrofit2.http.GET

interface MarvelService {
    @GET("characters?limit=5")
    suspend fun charactersAsync(): MarvelDataWrapper<MarvelCharacter>
}