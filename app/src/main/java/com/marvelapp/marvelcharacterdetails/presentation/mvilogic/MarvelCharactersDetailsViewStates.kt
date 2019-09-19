package com.marvelapp.marvelcharacterdetails.presentation.mvilogic

import com.marvelapp.entities.Results
import com.marvelapp.marvelcharacterdetails.entities.MarvelCharacterDetailsModel

sealed class MarvelCharactersDetailsViewStates {

    object OnLoadingPageDetailsState : MarvelCharactersDetailsViewStates()
    data class OpenMarvelCharacterImagesDialogState(
        val marvelCharacter: List<Results>,
        val position: Int
    ) : MarvelCharactersDetailsViewStates()

    object CloseMarvelCharacterImagesDialogState : MarvelCharactersDetailsViewStates()
    data class OnErrorPageDetailsState(val throwable: Throwable) : MarvelCharactersDetailsViewStates()
    data class OnSuccessPageDetailsState(val marvelCharacterDetailsModel: MarvelCharacterDetailsModel) :
        MarvelCharactersDetailsViewStates()

}
