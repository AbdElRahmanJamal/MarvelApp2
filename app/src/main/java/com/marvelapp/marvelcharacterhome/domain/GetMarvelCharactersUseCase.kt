package com.marvelapp.marvelcharacterhome.domain

import com.marvelapp.marvelcharacterhome.data.MarvelCharactersRepository
import com.marvelapp.entities.MarvelCharacters
import io.reactivex.Single

class GetMarvelCharactersUseCase(
    private val marvelCharactersRepository: MarvelCharactersRepository
) {

    fun getMarvelCharacters(limit: Int = 15, offset: Int = 0) =
        marvelCharactersRepository.getMarvelCharacters(limit, offset)

    fun getSearchMarvelCharactersList(name: String): Single<MarvelCharacters> =
        marvelCharactersRepository.getSearchMarvelCharactersList(name)

}