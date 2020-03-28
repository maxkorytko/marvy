package com.maxk.marvy.characters.repository

import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.model.marvel.PagedDataWrapper

class MarvelCharactersRepository {
    fun searchCharacters(characterName: String, pageSize: Int): PagedDataWrapper<MarvelCharacter> {
        val sourceFactory = MarvelCharactersDataSourceFactory(characterName)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()

        return PagedDataWrapper(
            pagedList = LivePagedListBuilder(sourceFactory, config).build(),
            pagingRequestStatus = sourceFactory.dataSource.switchMap {
                it.pagingRequestStatus
            }
        )
    }
}
