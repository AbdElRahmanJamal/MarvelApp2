package com.marvelapp.home.data

import com.marvelapp.frameworks.apiservice.MarvelApiService
import com.marvelapp.home.entities.MarvelCharacters
import io.reactivex.Single

class MarvelCharactersDataStore(private val marvelApiService: MarvelApiService) {

    fun getMarvelCharacters(limit: Int, offset: Int): Single<MarvelCharacters> {
        return marvelApiService.getMarvelCharacters(limit, offset)
    }
}