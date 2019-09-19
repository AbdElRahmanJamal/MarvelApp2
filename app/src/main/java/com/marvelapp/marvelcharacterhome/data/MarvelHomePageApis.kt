package com.marvelapp.marvelcharacterhome.data

import com.marvelapp.entities.MarvelCharacters
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelHomePageApis {

    @GET("v1/public/characters")
    fun getMarvelCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Single<MarvelCharacters>


    @GET("v1/public/characters")
    fun getSearchMarvelCharactersList(@Query("nameStartsWith") name: String): Single<MarvelCharacters>

}