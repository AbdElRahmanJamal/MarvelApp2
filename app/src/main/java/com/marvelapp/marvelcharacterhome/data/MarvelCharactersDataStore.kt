package com.marvelapp.marvelcharacterhome.data

import com.marvelapp.entities.MarvelCharacters
import io.reactivex.Single

class MarvelCharactersDataStore(private val marvelHomePageApis: MarvelHomePageApis) {

    fun getMarvelCharacters(limit: Int, offset: Int): Single<MarvelCharacters> =
        marvelHomePageApis.getMarvelCharacters(limit, offset)

    fun getSearchMarvelCharactersList(name: String): Single<MarvelCharacters> =
        marvelHomePageApis.getSearchMarvelCharactersList(name)
}