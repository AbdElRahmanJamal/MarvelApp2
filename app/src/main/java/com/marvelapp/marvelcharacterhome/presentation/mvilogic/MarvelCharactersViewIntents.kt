package com.marvelapp.marvelcharacterhome.presentation.mvilogic

sealed class MarvelCharactersHomeViewIntents {

    object LoadingMarvelCharactersIntent : MarvelCharactersHomeViewIntents()
    data class LoadingMoreMarvelCharactersIntent(val limit: Int = 15, val offset: Int) : MarvelCharactersHomeViewIntents()

}

sealed class MarvelCharactersSearchViewDialogIntents {

    data class SearchFieldChangeOfSearchDialogIntent(val searchName: String = "") :
        MarvelCharactersSearchViewDialogIntents()

    object CloseButtonOfSearchDialogClickedIntent : MarvelCharactersSearchViewDialogIntents()
    object SearchIconClickedIntent : MarvelCharactersSearchViewDialogIntents()

}