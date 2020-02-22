package com.maxk.marvy.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.maxk.marvy.api.marvel.MarvelApi
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.model.marvel.DataWrapper
import com.maxk.marvy.repository.MarvelRepository
import com.maxk.marvy.result.Complete
import com.maxk.marvy.result.Loading
import com.maxk.marvy.result.NetworkResource
import kotlinx.coroutines.Dispatchers
import com.maxk.marvy.result.Result

class MarvelCharactersViewModel(private val searchTerm: String): ViewModel() {

    class Factory(private val searchTerm: String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MarvelCharactersViewModel::class.java)) {
                return MarvelCharactersViewModel(searchTerm) as T
            }

            throw IllegalArgumentException("Unable to construct view model")
        }
    }

    private val repository = MarvelRepository(MarvelApi.client)

    val characters: LiveData<NetworkResource<DataWrapper<MarvelCharacter>>> = liveData(Dispatchers.IO) {
        emit(Loading())

        try {
            val characters = repository.searchCharacters(searchTerm)
            emit(Complete(Result.success(characters)))
        } catch (e: Exception) {
            Log.e(
                MarvelCharactersViewModel::class.java.simpleName,
                "Failed to fetch characters",
                e
            )
            emit(Complete(Result.error(e)))
        }
    }
}