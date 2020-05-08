package com.maxk.marvy.api.superhero

import com.maxk.marvy.api.SelfSignedCertificateTrustingClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://www.superheroapi.com/api/{api_key}/"

private val superHeroApiClient = SelfSignedCertificateTrustingClient.create()
    .newBuilder()
    .addInterceptor(SuperHeroApiAuth.interceptor)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(superHeroApiClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object SuperHeroApi {
    val client: SuperHeroService = retrofit.create(SuperHeroService::class.java)
}