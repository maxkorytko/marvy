package com.maxk.marvy.di

import com.maxk.marvy.api.marvel.MarvelApi
import com.maxk.marvy.api.marvel.MarvelService

interface ServiceLocator {
    companion object {
        lateinit var instance: ServiceLocator
            private set

        fun set(serviceLocator: ServiceLocator) {
            if (!::instance.isInitialized) {
                instance = serviceLocator
            }
        }
    }

    fun getMarvelService(): MarvelService
}

class DefaultServiceLocator : ServiceLocator {
    override fun getMarvelService(): MarvelService = MarvelApi.client
}