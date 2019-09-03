package com.marvelapp.marvelcharacterhome.data

import com.marvelapp.entities.MarvelCharacters
import com.marvelapp.frameworks.apiservice.interceptor.ConnectivityInterceptor
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
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