package com.marvelapp.home.data

import com.marvelapp.home.entities.MarvelCharacters
import io.reactivex.Single

class MarvelCharactersRepository(
    val marvelCharactersDataStore: MarvelCharactersDataStore
) {
    fun getMarvelCharacters(limit: Int, offset: Int): Single<MarvelCharacters> =
        marvelCharactersDataStore.getMarvelCharacters(limit, offset)

    fun getSearchMarvelCharactersList(name: String): Single<MarvelCharacters> =
        marvelCharactersDataStore.getSearchMarvelCharactersList(name)
}