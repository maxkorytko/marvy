package com.maxk.marvy.search

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.maxk.marvy.api.NetworkRequestStatus
import com.maxk.marvy.di.ServiceLocator
import com.maxk.marvy.extensions.filter
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.model.marvel.PagedDataWrapper
import com.maxk.marvy.model.marvel.PagedMetadata
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchableViewModel : ViewModel() {
    private var debounceJob: Job? = null

    private val pagedData: MutableLiveData<PagedDataWrapper<MarvelCharacter>> = MutableLiveData()

    val characters: LiveData<PagedList<MarvelCharacter>> = pagedData.switchMap { it.pagedList }

    val searchRequestStatus: LiveData<NetworkRequestStatus<PagedMetadata>> = pagedData
        .switchMap { it.pagingRequestStatus }
        .filter { it.isInitialRequest == true }

    val pagingRequestStatus: LiveData<NetworkRequestStatus<PagedMetadata>> = pagedData
        .switchMap { it.pagingRequestStatus }
        .filter { it.isInitialRequest == false }

    fun search(query: String?) {
        debounce { performSearch(query) }
    }

    private fun debounce(timeoutMs: Long = 500, action: () -> Unit) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(timeoutMs)
            action()
        }
    }

    private fun performSearch(query: String?) {
        if (query == null) return

        val repository = ServiceLocator.instance.getMarvelCharactersRepository()
        pagedData.postValue(repository.searchCharacters(query))
    }
}