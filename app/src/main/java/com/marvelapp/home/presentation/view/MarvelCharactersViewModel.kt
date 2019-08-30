package com.marvelapp.home.presentation.view

import androidx.lifecycle.ViewModel
import com.marvelapp.home.domain.GetMarvelCharactersUseCase
import com.marvelapp.home.presentation.mvilogic.MarvelCharactersViewIntents
import com.marvelapp.home.presentation.mvilogic.MarvelCharactersViewStates
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MarvelCharactersViewModel(private val getMarvelCharactersUseCase: GetMarvelCharactersUseCase) : ViewModel() {

    fun getMarvelHomePageCharacters(intents: Observable<out MarvelCharactersViewIntents>)
            : Observable<MarvelCharactersViewStates> {

        return intents.flatMap {
            when (it) {
                is MarvelCharactersViewIntents.GetMarvelCharactersIntent -> {
                    getMarvelCharacters()
                }
                is MarvelCharactersViewIntents.GetMoreMarvelCharactersIntent -> {
                    getLoadMoreMarvelCharacters(limit = it.limit, offset = it.offset)
                }
                is MarvelCharactersViewIntents.onSearchIconClickedIntent -> {
                    Observable.just(MarvelCharactersViewStates.ShowSearchResultDialog)
                }
                is MarvelCharactersViewIntents.onSearchFieldChangeOfSearchDialogIntent -> {
                    getSearchMarvelCharactersList(it.searchName)
                }
                is MarvelCharactersViewIntents.onCloseButtonOfSearchDialogClicked -> {
                    Observable.just(MarvelCharactersViewStates.CloseSearchResultDialog)
                }

            }
        }.distinctUntilChanged()
    }

    private fun getMarvelCharacters(): Observable<MarvelCharactersViewStates> {
        return getMarvelCharactersUseCase.getMarvelCharacters()
            .toObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { MarvelCharactersViewStates.SuccessState(it) }
            .cast(MarvelCharactersViewStates::class.java)
            .startWith(MarvelCharactersViewStates.LoadingForGettingMarvelCharachtersState)
            .onErrorReturn { MarvelCharactersViewStates.ErrorState(it) }

    }

    private fun getLoadMoreMarvelCharacters(limit: Int = 15, offset: Int = 0): Observable<MarvelCharactersViewStates> {
        return getMarvelCharacters(limit, offset)
            .startWith(MarvelCharactersViewStates.LoadMoreMarvelCharactersViewState)
            .onErrorReturn { MarvelCharactersViewStates.HideLoadMoreViewState }

    }

    private fun getMarvelCharacters(
        limit: Int,
        offset: Int
    ): Observable<MarvelCharactersViewStates> {
        return getMarvelCharactersUseCase.getMarvelCharacters(limit, offset)
            .toObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { MarvelCharactersViewStates.SuccessState(it) }
            .cast(MarvelCharactersViewStates::class.java)
    }


    fun getSearchMarvelCharactersList(name: String): Observable<MarvelCharactersViewStates>? =
        getMarvelCharactersUseCase.getSearchMarvelCharactersList(name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .toObservable()
            .map { MarvelCharactersViewStates.SuccessForSearchResultState(it) }
            .cast(MarvelCharactersViewStates::class.java)
            .startWith(MarvelCharactersViewStates.ShowLoadingForSearchForCharacterByNameState)
            .onErrorReturn { MarvelCharactersViewStates.ErrorState(it) }
}