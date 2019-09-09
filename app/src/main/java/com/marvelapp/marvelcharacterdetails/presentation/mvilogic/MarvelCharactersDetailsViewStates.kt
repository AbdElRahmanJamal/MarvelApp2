package com.marvelapp.marvelcharacterdetails.presentation.mvilogic

import com.marvelapp.marvelcharacterdetails.entities.MarvelCharacterDetailsModel
import java.text.FieldPosition

sealed class MarvelCharactersDetailsViewStates {

    object OnLoadingPageDetailsState : MarvelCharactersDetailsViewStates()
    object OpenMarvelCharacterImagesDialogState : MarvelCharactersDetailsViewStates()
    object CloseMarvelCharacterImagesDialogState : MarvelCharactersDetailsViewStates()
    data class OnErrorPageDetailsState(val throwable: Throwable) : MarvelCharactersDetailsViewStates()
    data class OnSuccessPageDetailsState(val marvelCharacterDetailsModel: MarvelCharacterDetailsModel) :
        MarvelCharactersDetailsViewStates()

}
