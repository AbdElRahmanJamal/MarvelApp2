package com.marvelapp.marvelcharacterdetails.data

class MarvelCharacterDetailsDataStore(private val marvelDetailsPageApis: MarvelDetailsPageApis) {

    fun getMarvelCharacterComicsList(characterId: Int) = marvelDetailsPageApis.getMarvelCharacterComicsList(characterId)


    fun getMarvelCharacterSeriesList(characterId: Int) = marvelDetailsPageApis.getMarvelCharacterSeriesList(characterId)


    fun getMarvelCharacterStoriesList(characterId: Int) = marvelDetailsPageApis.getMarvelCharacterStoriesList(characterId)


    fun getMarvelCharacterEventsList(characterId: Int) = marvelDetailsPageApis.getMarvelCharacterEventsList(characterId)

}