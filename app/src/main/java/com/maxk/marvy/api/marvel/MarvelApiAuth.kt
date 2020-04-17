package com.maxk.marvy.api.marvel

import com.maxk.marvy.BuildConfig
import okhttp3.Interceptor
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

object MarvelApiAuth {
    private const val publicKey = BuildConfig.MARVEL_PUBLIC_KEY
    private const val privateKey = BuildConfig.MARVEL_PRIVATE_KEY

    val interceptor = Interceptor { chain ->
        val timestamp = Date().time
        val hash = "$timestamp$privateKey$publicKey".md5()

        val request = chain.request()

        val requestUrl = request.url().newBuilder()
            .addQueryParameter("apikey", publicKey)
            .addQueryParameter("ts", "$timestamp")
            .addQueryParameter("hash", hash)
            .build()

        chain.proceed(request.newBuilder().url(requestUrl).build())
    }
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}