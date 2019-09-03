package com.marvelapp.marvelcharacterdetails.data

class MarvelCharacterDetailsRepository(
    private val marvelCharacterDetailsDataStore: MarvelCharacterDetailsDataStore
) {

    fun getMarvelCharacterComicsList(characterId: Int) =
        marvelCharacterDetailsDataStore.getMarvelCharacterComicsList(characterId)


    fun getMarvelCharacterSeriesList(characterId: Int) =
        marvelCharacterDetailsDataStore.getMarvelCharacterSeriesList(characterId)


    fun getMarvelCharacterStoriesList(characterId: Int) =
        marvelCharacterDetailsDataStore.getMarvelCharacterStoriesList(characterId)


    fun getMarvelCharacterEventsList(characterId: Int) =
        marvelCharacterDetailsDataStore.getMarvelCharacterEventsList(characterId)
}