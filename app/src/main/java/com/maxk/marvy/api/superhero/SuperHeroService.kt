package com.maxk.marvy.api.superhero

import com.maxk.marvy.model.superhero.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SuperHeroService {
    @GET("search/{name}")
    suspend fun searchSuperHeroesAsync(@Path("name") name: String): SearchResponse
}