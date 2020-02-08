package com.maxk.marvy.api.marvel

import com.maxk.marvy.model.marvel.Character
import com.maxk.marvy.model.marvel.DataWrapper
import retrofit2.http.GET

interface MarvelService {
    @GET("characters?limit=5")
    suspend fun charactersAsync(): DataWrapper<Character>
}