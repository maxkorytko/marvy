package com.maxk.marvy.api.marvel

import com.maxk.marvy.model.marvel.Character
import com.maxk.marvy.model.marvel.DataWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {
    @GET("characters?limit=5")
    suspend fun searchCharactersAsync(@Query("nameStartsWith") searchTerm: String)
            : DataWrapper<Character>
}