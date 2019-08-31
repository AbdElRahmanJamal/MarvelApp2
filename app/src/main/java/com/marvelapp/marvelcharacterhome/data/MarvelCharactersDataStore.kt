package com.marvelapp.marvelcharacterhome.data

import com.marvelapp.frameworks.apiservice.MarvelApiService
import com.marvelapp.entities.MarvelCharacters
import io.reactivex.Single

class MarvelCharactersDataStore(private val marvelApiService: MarvelApiService) {

    fun getMarvelCharacters(limit: Int, offset: Int): Single<MarvelCharacters> =
        marvelApiService.getMarvelCharacters(limit, offset)

    fun getSearchMarvelCharactersList(name: String): Single<MarvelCharacters> =
        marvelApiService.getSearchMarvelCharactersList(name)
}