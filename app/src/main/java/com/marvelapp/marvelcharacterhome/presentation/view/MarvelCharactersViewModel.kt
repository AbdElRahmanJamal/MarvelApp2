package com.marvelapp.marvelcharacterhome.presentation.view

import androidx.lifecycle.ViewModel
import com.marvelapp.marvelcharacterhome.domain.GetMarvelCharactersUseCase
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersHomeViewIntents
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersHomeViewStates
import com.marvelapp.marvelcharacterhome.presentation.mvilogic.MarvelCharactersSearchDialogViewStates
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MarvelCharactersViewModel(private val getMarvelCharactersUseCase: GetMarvelCharactersUseCase) : ViewModel() {

    fun getMarvelHomePageCharacters(intents: Observable<out MarvelCharactersHomeViewIntents>)
            : Observable<MarvelCharactersHomeViewStates> {

        return intents.flatMap {
            when (it) {
                is MarvelCharactersHomeViewIntents.LoadingMarvelCharactersIntent -> {
                    getMarvelCharacters()
                }
                is MarvelCharactersHomeViewIntents.LoadingMoreMarvelCharactersIntent -> {
                    getLoadMoreMarvelCharacters(limit = it.limit, offset = it.offset)
                }
            }
        }.distinctUntilChanged()
    }

    private fun getMarvelCharacters(): Observable<MarvelCharactersHomeViewStates> {
        return gettingMarvelCharacters()
            .cast(MarvelCharactersHomeViewStates::class.java)
            .startWith(MarvelCharactersHomeViewStates.ShowLoadingMarvelCharactersState)
            .onErrorReturn { MarvelCharactersHomeViewStates.ErrorState(it) }

    }

    private fun getLoadMoreMarvelCharacters(
        limit: Int = 15,
        offset: Int = 0
    ): Observable<MarvelCharactersHomeViewStates> {
        return gettingMarvelCharacters(limit, offset)
            .startWith(MarvelCharactersHomeViewStates.ShowLoadMoreMarvelCharactersViewState)
            .onErrorReturn { MarvelCharactersHomeViewStates.HideLoadMoreMarvelCharactersViewState }

    }

    private fun gettingMarvelCharacters(
        limit: Int = 15,
        offset: Int = 0
    ): Observable<MarvelCharactersHomeViewStates> {
        return getMarvelCharactersUseCase.getMarvelCharacters(limit, offset)
            .toObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { MarvelCharactersHomeViewStates.SuccessState(it) }
            .cast(MarvelCharactersHomeViewStates::class.java)
    }


    fun getSearchForMarvelCharacterList(name: String): Observable<MarvelCharactersSearchDialogViewStates>? =
        getMarvelCharactersUseCase.getSearchMarvelCharactersList(name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .toObservable()
            .map { MarvelCharactersSearchDialogViewStates.SuccessForSearchResultState(it) }
            .cast(MarvelCharactersSearchDialogViewStates::class.java)
            .startWith(MarvelCharactersSearchDialogViewStates.ShowLoadingSearchForCharacterByNameStateState)
            .onErrorReturn { MarvelCharactersSearchDialogViewStates.HideLoadingSearchForCharacterByNameStateState }
}