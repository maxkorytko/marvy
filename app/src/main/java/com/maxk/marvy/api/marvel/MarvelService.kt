package com.maxk.marvy.api.marvel

import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.model.marvel.DataWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelService {
    @GET("characters?orderBy=name")
    suspend fun searchCharactersAsync(@Query("nameStartsWith") searchTerm: String,
                                      @Query("limit") pageSize: Int,
                                      @Query("offset") offset: Int = 0)
            : DataWrapper<MarvelCharacter>
}