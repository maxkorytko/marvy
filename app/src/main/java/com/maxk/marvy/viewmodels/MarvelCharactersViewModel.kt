package com.maxk.marvy.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.maxk.marvy.api.marvel.MarvelApi
import com.maxk.marvy.model.marvel.Character
import com.maxk.marvy.model.marvel.DataWrapper
import com.maxk.marvy.repository.MarvelRepository
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

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val characters: LiveData<Result<DataWrapper<Character>>> = liveData(Dispatchers.IO) {
        try {
            _isLoading.postValue(true)
            emit(Result.success(repository.searchCharacters(searchTerm)))
        } catch (e: Exception) {
            Log.e(
                MarvelCharactersViewModel::class.java.simpleName,
                "Failed to fetch characters",
                e
            )
            emit(Result.error(e))
        } finally {
            _isLoading.postValue(false)
        }
    }
}