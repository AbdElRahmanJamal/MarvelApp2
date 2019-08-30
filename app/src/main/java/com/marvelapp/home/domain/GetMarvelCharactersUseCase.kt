package com.marvelapp.home.domain

import com.marvelapp.home.data.MarvelCharactersRepository
import com.marvelapp.home.entities.MarvelCharacters
import io.reactivex.Single

class GetMarvelCharactersUseCase(
    private val marvelCharactersRepository: MarvelCharactersRepository
) {

    fun getMarvelCharacters(limit: Int = 15, offset: Int = 0) =
        marvelCharactersRepository.getMarvelCharacters(limit, offset)

    fun getSearchMarvelCharactersList(name: String): Single<MarvelCharacters> =
        marvelCharactersRepository.getSearchMarvelCharactersList(name)

}