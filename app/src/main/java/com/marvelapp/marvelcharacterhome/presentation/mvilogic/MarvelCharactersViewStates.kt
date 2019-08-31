package com.marvelapp.marvelcharacterhome.presentation.mvilogic

import com.marvelapp.entities.MarvelCharacters

sealed class MarvelCharactersHomeViewStates {

    object ShowLoadingMarvelCharactersState : MarvelCharactersHomeViewStates()
    data class ErrorState(val throwable: Throwable) : MarvelCharactersHomeViewStates()
    data class SuccessState(val marvelCharacters: MarvelCharacters) : MarvelCharactersHomeViewStates()
    object HideLoadMoreMarvelCharactersViewState : MarvelCharactersHomeViewStates()
    object ShowLoadMoreMarvelCharactersViewState : MarvelCharactersHomeViewStates()
}

sealed class MarvelCharactersSearchDialogViewStates {

    object CloseSearchResultDialogState : MarvelCharactersSearchDialogViewStates()
    object ShowSearchResultDialogState : MarvelCharactersSearchDialogViewStates()
    object ShowLoadingSearchForCharacterByNameStateState : MarvelCharactersSearchDialogViewStates()
    object HideLoadingSearchForCharacterByNameStateState : MarvelCharactersSearchDialogViewStates()
    data class SuccessForSearchResultState(val marvelCharacters: MarvelCharacters) :
        MarvelCharactersSearchDialogViewStates()

}