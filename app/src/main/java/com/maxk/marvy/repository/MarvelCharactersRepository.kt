package com.maxk.marvy.repository

import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import com.maxk.marvy.model.marvel.MarvelCharacter
import com.maxk.marvy.model.marvel.PagedDataWrapper

class MarvelCharactersRepository {
    fun searchCharacters(characterName: String, pageSize: Int): PagedDataWrapper<MarvelCharacter> {
        val sourceFactory = MarvelCharactersDataSourceFactory(characterName)

        return PagedDataWrapper(
            pagedList = sourceFactory.toLiveData(pageSize),
            pagingRequestStatus = sourceFactory.dataSource.switchMap {
                it.pagingRequestStatus
            }
        )
    }
}
