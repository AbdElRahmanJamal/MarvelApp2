package com.marvelapp.home.data

import com.marvelapp.home.entities.MarvelCharacters
import io.reactivex.Single

class MarvelCharactersRepository(
    val marvelCharactersDataStore: MarvelCharactersDataStore
) {
    fun getMarvelCharacters(limit: Int, offset: Int): Single<MarvelCharacters> {
        return marvelCharactersDataStore.getMarvelCharacters(limit, offset)
    }
}