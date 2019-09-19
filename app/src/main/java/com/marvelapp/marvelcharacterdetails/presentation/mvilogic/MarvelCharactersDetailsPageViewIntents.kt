package com.marvelapp.marvelcharacterdetails.presentation.mvilogic

import com.marvelapp.entities.Results

sealed class MarvelCharactersDetailsPageViewIntents {

    data class OnDetailsPageStartIntent(val charID: Int) : MarvelCharactersDetailsPageViewIntents()
    object CloseButtonOfSearchDialogClickedIntent : MarvelCharactersDetailsPageViewIntents()
    data class OnDetailItemClickedIntent(
        val marvelCharacterDetailsModel: List<Results>
        , val position: Int
    ) : MarvelCharactersDetailsPageViewIntents()


}

