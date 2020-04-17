package com.maxk.marvy.api.superhero

import com.maxk.marvy.BuildConfig
import okhttp3.Interceptor

object SuperHeroApiAuth {
    private const val apiKey = BuildConfig.SUPER_HERO_API_KEY

    val interceptor = Interceptor { chain ->
        val request = chain.request()

        val requestUrl = request.url()
        val requestUrlBuilder = requestUrl.newBuilder()

        val apiKeyPathSegmentIndex = requestUrl.pathSegments().indexOfFirst { it == "{api_key}" }
        if (apiKeyPathSegmentIndex != -1) {
            requestUrlBuilder.setPathSegment(apiKeyPathSegmentIndex, apiKey)
        }

        chain.proceed(request.newBuilder().url(requestUrlBuilder.build()).build())
    }
}