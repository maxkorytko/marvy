package com.maxk.marvy.api.marvel

import okhttp3.Interceptor
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

object MarvelApiAuth {
    private const val publicKey = "f1063bf3f9f2d26309c1591290530dd2"
    private const val privateKey = "6c740fa4f583f9332713bd755f1af15c5ae4d0bd"

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