package com.maxk.marvy.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharactersDataSourceFactory(private val characterName: String)
    : DataSource.Factory<Int, MarvelCharacter>() {

    val dataSource = MutableLiveData<MarvelCharactersDataSource>()

    override fun create(): DataSource<Int, MarvelCharacter> {
        val source = MarvelCharactersDataSource(characterName)
        dataSource.postValue(source)
        return source
    }
}