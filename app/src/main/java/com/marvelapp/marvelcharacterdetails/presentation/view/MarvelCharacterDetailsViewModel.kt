package com.marvelapp.marvelcharacterdetails.presentation.view

import androidx.lifecycle.ViewModel
import com.marvelapp.marvelcharacterdetails.domain.GetMarvelCharacterDetailsUseCase
import com.marvelapp.marvelcharacterdetails.presentation.mvilogic.MarvelCharactersDetailsPageViewIntents
import com.marvelapp.marvelcharacterdetails.presentation.mvilogic.MarvelCharactersDetailsViewStates
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MarvelCharacterDetailsViewModel(private val getMarvelCharacterDetailsUseCase: GetMarvelCharacterDetailsUseCase) :
    ViewModel() {


    fun startMarvelCharacterDetailsPage(intents: Observable<out MarvelCharactersDetailsPageViewIntents>)
            : Observable<MarvelCharactersDetailsViewStates> {

        return intents.flatMap {
            when (it) {
                is MarvelCharactersDetailsPageViewIntents.OnDetailsPageStartIntent -> {
                    getMarvelCharacterDetails(it.charID)
                }
                is MarvelCharactersDetailsPageViewIntents.OnDetailItemClickedIntent -> {
                    Observable.just(
                        MarvelCharactersDetailsViewStates
                            .OpenMarvelCharacterImagesDialogState(it.marvelCharacterDetailsModel, it.position)
                    )
                }
                is MarvelCharactersDetailsPageViewIntents.CloseButtonOfSearchDialogClickedIntent -> {
                    Observable.just(MarvelCharactersDetailsViewStates.CloseMarvelCharacterImagesDialogState)
                }

            }
        }.distinctUntilChanged()
    }

    private fun getMarvelCharacterDetails(charID: Int): Observable<MarvelCharactersDetailsViewStates>? {

        return getMarvelCharacterDetailsUseCase.getMarvelCharacterDetailsComicsSeriesStoriesEvents(charID)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { MarvelCharactersDetailsViewStates.OnSuccessPageDetailsState(it) }
            .cast(MarvelCharactersDetailsViewStates::class.java)
            .startWith(MarvelCharactersDetailsViewStates.OnLoadingPageDetailsState)
            .onErrorReturn {
                (MarvelCharactersDetailsViewStates.OnErrorPageDetailsState(it))
            }
    }
}

