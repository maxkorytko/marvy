package com.maxk.marvy.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.maxk.marvy.api.marvel.MarvelService
import com.maxk.marvy.di.ServiceLocator
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.result.Complete
import com.maxk.marvy.result.Loading
import com.maxk.marvy.result.NetworkRequestStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MarvelCharactersDataSource(private val characterName: String)
    : PositionalDataSource<MarvelCharacter>() {

    private val job: Job = Job()
    private val dataSourceScope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)
    private val marvelService: MarvelService = ServiceLocator.instance.getMarvelService()

    val pagingRequestStatus = MutableLiveData<NetworkRequestStatus<Unit>>()

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
            } catch (e: Exception) {
                Log.e(
                    MarvelCharactersDataSource::class.java.simpleName,
                    "Failed to fetch characters",
                    e
                )
            } finally {
                notifyDoneLoading(isInitialRequest = false)
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
                    characters.data.offset ?: 0,
                    characters.data.total ?: 0
                )
            } catch (e: Exception) {
                Log.e(
                    MarvelCharactersDataSource::class.java.simpleName,
                    "Failed to fetch characters (initial request)",
                    e
                )
            } finally {
                notifyDoneLoading(isInitialRequest = true)
            }
        }
    }

    private fun notifyLoading(isInitialRequest: Boolean) {
        pagingRequestStatus.postValue(Loading(isInitialRequest))
    }

    private fun notifyDoneLoading(isInitialRequest: Boolean) {
        pagingRequestStatus.postValue(
            Complete(isInitialRequest, Result.success(Unit))
        )
    }

    override fun invalidate() {
        job.cancel()
        super.invalidate()
    }
}