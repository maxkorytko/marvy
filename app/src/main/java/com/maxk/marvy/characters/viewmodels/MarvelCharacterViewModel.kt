package com.maxk.marvy.characters.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.maxk.marvy.api.Loading
import com.maxk.marvy.api.NetworkRequestStatus
import com.maxk.marvy.model.CharacterInfo
import com.maxk.marvy.model.marvel.Image
import com.maxk.marvy.model.marvel.MarvelCharacter

class MarvelCharacterViewModel(character: MarvelCharacter?): ViewModel() {
    class Factory(private val character: MarvelCharacter?): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MarvelCharacterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MarvelCharacterViewModel(character) as T
            }

            throw IllegalArgumentException("Unable to construct view model")
        }
    }

    val title: String? = character?.name

    var description: String? = character?.description

    var image: Image? = character?.thumbnail

    val additionalInfo = liveData<NetworkRequestStatus<List<CharacterInfo>>> {
        emit(Loading())
    }
}