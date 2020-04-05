package com.maxk.marvy.characters.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.maxk.marvy.api.Complete
import com.maxk.marvy.api.Loading
import com.maxk.marvy.api.NetworkRequestStatus
import com.maxk.marvy.api.marvel.MarvelService
import com.maxk.marvy.di.ServiceLocator
import com.maxk.marvy.model.marvel.DataWrapper
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.model.marvel.PagedMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MarvelCharactersDataSource(private val characterName: String)
    : PositionalDataSource<MarvelCharacter>() {

    private val job: Job = Job()
    private val dataSourceScope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)
    private val marvelService: MarvelService = ServiceLocator.instance.getMarvelService()

    val pagingRequestStatus = MutableLiveData<NetworkRequestStatus<PagedMetadata>>()

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<MarvelCharacter>) {
        notifyLoading(isInitialRequest = false)

        dataSourceScope.launch {
            try {
                val characters = marvelService.searchCharactersAsync(
                    characterName,
                    params.loadSize,
                    params.startPosition
                )
                callback.onResult(characters.data.results)
                notifySuccess(isInitialRequest = false, characters = characters)
            } catch (e: Exception) {
                Log.e(
                    MarvelCharactersDataSource::class.java.simpleName,
                    "Failed to fetch characters",
                    e
                )
                notifyError(isInitialRequest = false, error = e)
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams,
                             callback: LoadInitialCallback<MarvelCharacter>) {

        notifyLoading(isInitialRequest = true)

        dataSourceScope.launch {
            try {
                val characters = marvelService.searchCharactersAsync(characterName, params.pageSize)

                callback.onResult(
                    characters.data.results,
                    characters.data.offset ?: 0
                )

                notifySuccess(isInitialRequest = true, characters = characters)
            } catch (e: Exception) {
                Log.e(
                    MarvelCharactersDataSource::class.java.simpleName,
                    "Failed to fetch characters (initial request)",
                    e
                )
                notifyError(isInitialRequest = true, error = e)
            }
        }
    }

    private fun notifyLoading(isInitialRequest: Boolean) {
        pagingRequestStatus.postValue(Loading(isInitialRequest))
    }

    private fun notifySuccess(isInitialRequest: Boolean,
                              characters: DataWrapper<MarvelCharacter>) {

        val result = Result.success(PagedMetadata(
            itemsFetched = characters.data.results.size
        ))

        pagingRequestStatus.postValue(
            Complete(isInitialRequest, result)
        )
    }

    private fun notifyError(isInitialRequest: Boolean, error: Throwable) {
        pagingRequestStatus.postValue(Complete(isInitialRequest, Result.failure(error)))
    }

    override fun invalidate() {
        job.cancel()
        super.invalidate()
    }
}