package com.maxk.marvy.characters.repository

import com.maxk.marvy.api.superhero.SuperHeroApi
import com.maxk.marvy.extensions.urlEncoded
import com.maxk.marvy.model.CharacterInfo

class CharacterInfoRepository {
    suspend fun retrieveCharacterInfo(characterName: String?): Result<List<CharacterInfo>> {
        if (characterName == null) {
            return Result.failure(IllegalArgumentException("character name is null"))
        }

        return try {
            val response = SuperHeroApi.client.searchSuperHeroesAsync(characterName.urlEncoded())

            if (response.error != null) {
                throw Exception(response.error)
            }

            Result.success(response.results ?: emptyList())
        } catch(e: Exception) {
            Result.failure(e)
        }
    }
}