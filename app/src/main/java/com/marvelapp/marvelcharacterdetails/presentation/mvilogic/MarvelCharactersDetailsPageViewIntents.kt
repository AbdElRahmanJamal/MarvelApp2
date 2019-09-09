package com.marvelapp.marvelcharacterdetails.presentation.mvilogic

sealed class MarvelCharactersDetailsPageViewIntents {

    data class OnDetailsPageStartIntent(val charID: Int) : MarvelCharactersDetailsPageViewIntents()
    object CloseButtonOfSearchDialogClickedIntent : MarvelCharactersDetailsPageViewIntents()
    object OnDetailItemClickedIntent : MarvelCharactersDetailsPageViewIntents()


}

