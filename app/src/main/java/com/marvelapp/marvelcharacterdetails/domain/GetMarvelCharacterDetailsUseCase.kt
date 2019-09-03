package com.marvelapp.marvelcharacterdetails.domain

import com.marvelapp.entities.MarvelCharacters
import com.marvelapp.marvelcharacterdetails.data.MarvelCharacterDetailsRepository
import com.marvelapp.marvelcharacterdetails.entities.MarvelCharacterDetailsModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function4

class GetMarvelCharacterDetailsUseCase(
    private val MarvelCharacterDetailsRepository: MarvelCharacterDetailsRepository
) {

    fun getMarvelCharacterDetailsComicsSeriesStoriesEvents(characterId: Int): Observable<MarvelCharacterDetailsModel> {
        val marvelCharacterComicsList =
            applyOperationOnDetailsPageData(MarvelCharacterDetailsRepository.getMarvelCharacterComicsList(characterId))
        val marvelCharacterSeriesList =
            applyOperationOnDetailsPageData(MarvelCharacterDetailsRepository.getMarvelCharacterSeriesList(characterId))
        val marvelCharacterStoriesList =
            applyOperationOnDetailsPageData(MarvelCharacterDetailsRepository.getMarvelCharacterStoriesList(characterId))
        val marvelCharacterEventsList =
            applyOperationOnDetailsPageData(MarvelCharacterDetailsRepository.getMarvelCharacterEventsList(characterId))

        return Observable.zip(
            marvelCharacterComicsList,
            marvelCharacterSeriesList,
            marvelCharacterStoriesList,
            marvelCharacterEventsList,
            Function4 { t1, t2,
                        t3, t4 ->
                MarvelCharacterDetailsModel(
                    "Comics" to t1, "Series" to t2, "Stories" to t3, "Events" to t4
                )
            }
        )
    }

    private fun applyOperationOnDetailsPageData(marvelCharacter: Single<MarvelCharacters>) =
        marvelCharacter.toObservable()
            .flatMapIterable { marvelCharacters -> marvelCharacters.data.results }
            .filter { item -> item.thumbnail != null }
            .toList()
            .toObservable()
            .defaultIfEmpty(mutableListOf())
}
