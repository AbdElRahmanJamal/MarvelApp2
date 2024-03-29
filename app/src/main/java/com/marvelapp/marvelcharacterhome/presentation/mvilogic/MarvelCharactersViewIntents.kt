package com.marvelapp.marvelcharacterhome.presentation.mvilogic

import com.marvelapp.entities.Results

sealed class MarvelCharactersHomeViewIntents {

    object LoadingMarvelCharactersIntent : MarvelCharactersHomeViewIntents()
    data class LoadingMoreMarvelCharactersIntent(val limit: Int = 15, val offset: Int) :
        MarvelCharactersHomeViewIntents()

    data class GoToMarvelCharacterDetailsPageIntent(val marvelCharacter: Results) :
        MarvelCharactersHomeViewIntents()

    object SearchIconClickedIntent : MarvelCharactersHomeViewIntents()
    object CloseButtonOfSearchDialogClickedIntent : MarvelCharactersHomeViewIntents()
}

sealed class MarvelCharactersSearchViewDialogIntents {

    data class SearchFieldChangeOfSearchDialogIntent(val searchName: String = "") :
        MarvelCharactersSearchViewDialogIntents()


    data class GoToMarvelCharacterDetailsPageIntent(val marvelCharacter: Results) :
        MarvelCharactersSearchViewDialogIntents()

}