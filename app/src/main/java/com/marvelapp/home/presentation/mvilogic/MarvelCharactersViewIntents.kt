package com.marvelapp.home.presentation.mvilogic

sealed class MarvelCharactersViewIntents {

    object GetMarvelCharactersIntent : MarvelCharactersViewIntents()
    data class GetMoreMarvelCharactersIntent(val limit: Int = 15, val offset: Int) : MarvelCharactersViewIntents()
}