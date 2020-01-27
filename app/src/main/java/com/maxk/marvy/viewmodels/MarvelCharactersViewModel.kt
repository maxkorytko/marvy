package com.maxk.marvy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.maxk.marvy.api.marvel.MarvelApi
import com.maxk.marvy.model.MarvelCharacter
import com.maxk.marvy.model.MarvelDataWrapper
import com.maxk.marvy.repository.MarvelRepository
import kotlinx.coroutines.Dispatchers

class MarvelCharactersViewModel: ViewModel() {
    private val repository = MarvelRepository(MarvelApi.client)

    val characters: LiveData<MarvelDataWrapper<MarvelCharacter>> = liveData(Dispatchers.IO) {
        emit(repository.fetchCharacters())
    }
}