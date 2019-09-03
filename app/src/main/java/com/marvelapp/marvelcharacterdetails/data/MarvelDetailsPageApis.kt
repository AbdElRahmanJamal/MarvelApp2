package com.marvelapp.marvelcharacterdetails.data

import com.marvelapp.entities.MarvelCharacters
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MarvelDetailsPageApis {

    @GET("/v1/public/characters/{characterId}/comics")
    fun getMarvelCharacterComicsList(@Path("characterId") characterId: Int): Single<MarvelCharacters>

    @GET("/v1/public/characters/{characterId}/series")
    fun getMarvelCharacterSeriesList(@Path("characterId") characterId: Int): Single<MarvelCharacters>

    @GET("/v1/public/characters/{characterId}/stories")
    fun getMarvelCharacterStoriesList(@Path("characterId") characterId: Int): Single<MarvelCharacters>

    @GET("/v1/public/characters/{characterId}/events")
    fun getMarvelCharacterEventsList(@Path("characterId") characterId: Int): Single<MarvelCharacters>
}