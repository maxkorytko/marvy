package com.maxk.marvy.api.marvel

import com.maxk.marvy.api.SelfSignedCertificateTrustingClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://gateway.marvel.com/v1/public/"

private val marvelApiClient = SelfSignedCertificateTrustingClient.create()
    .newBuilder()
    .addInterceptor(MarvelApiAuth.interceptor)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(marvelApiClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object MarvelApi {
    val client: MarvelService = retrofit.create(MarvelService::class.java)
}