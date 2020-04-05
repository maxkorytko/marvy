package com.maxk.marvy.characters.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.maxk.marvy.api.NetworkRequestStatus
import com.maxk.marvy.di.ServiceLocator
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.model.marvel.PagedMetadata

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

    private val repository = ServiceLocator.instance.getMarvelCharactersRepository()

    private val pagedData = repository.searchCharacters(searchTerm)

    val pagingRequestStatus: LiveData<NetworkRequestStatus<PagedMetadata>> = pagedData.pagingRequestStatus

    val characters: LiveData<PagedList<MarvelCharacter>> = pagedData.pagedList
}