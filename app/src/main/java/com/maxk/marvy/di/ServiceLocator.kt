package com.maxk.marvy.di

import com.maxk.marvy.api.marvel.MarvelApi
import com.maxk.marvy.api.marvel.MarvelService
import com.maxk.marvy.characters.repository.CharacterInfoRepository
import com.maxk.marvy.characters.repository.MarvelCharactersRepository

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
    fun getMarvelCharactersRepository(): MarvelCharactersRepository
    fun getCharacterInfoRepository(): CharacterInfoRepository
}

class DefaultServiceLocator : ServiceLocator {
    override fun getMarvelService(): MarvelService = MarvelApi.client

    override fun getMarvelCharactersRepository(): MarvelCharactersRepository {
        return MarvelCharactersRepository()
    }

    override fun getCharacterInfoRepository(): CharacterInfoRepository {
        return CharacterInfoRepository()
    }
}