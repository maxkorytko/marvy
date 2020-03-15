package com.maxk.marvy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.repository.MarvelCharactersRepository
import com.maxk.marvy.result.NetworkRequestStatus

class MarvelCharactersViewModel(private val searchTerm: String): ViewModel() {
    class Factory(private val searchTerm: String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MarvelCharactersViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MarvelCharactersViewModel(searchTerm) as T
            }

            throw IllegalArgumentException("Unable to construct view model")
        }
    }

    private val repository = MarvelCharactersRepository()

    private val pagedData = repository.searchCharacters(searchTerm, 10)

    val pagingRequestStatus: LiveData<NetworkRequestStatus<Unit>> = pagedData.pagingRequestStatus

    val characters: LiveData<PagedList<MarvelCharacter>> = pagedData.pagedList
}