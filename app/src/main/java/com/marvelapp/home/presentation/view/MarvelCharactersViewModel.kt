package com.marvelapp.home.presentation.view

import androidx.lifecycle.ViewModel
import com.marvelapp.home.domain.GetMarvelCharactersUseCase
import com.marvelapp.home.presentation.mvilogic.MarvelCharactersViewIntents
import com.marvelapp.home.presentation.mvilogic.MarvelCharactersViewStates
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MarvelCharactersViewModel(private val getMarvelCharactersUseCase: GetMarvelCharactersUseCase)
    : ViewModel() {

    fun getMarvelHomePageCharacters(intents: Observable<out MarvelCharactersViewIntents>)
            : Observable<MarvelCharactersViewStates> {
        return intents.flatMap {
            when (it) {
                is MarvelCharactersViewIntents.GetMarvelCharactersIntent -> {
                    getMarvelCharacters()
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
                .cast(MarvelCharactersViewStates::class.java)//to convert player info to view state
                .startWith(MarvelCharactersViewStates.LoadingState)
                .onErrorReturn { MarvelCharactersViewStates.ErrorState(it) }

    }
}