package com.marvelapp.frameworks.apiservice

import com.marvelapp.frameworks.apiservice.interceptor.ConnectivityInterceptor
import com.marvelapp.home.entities.MarvelCharacters
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApiService {
    @GET("v1/public/characters")
    fun getMarvelCharacters(
            @Query("limit") limit: Int,
            @Query("offset") offset: Int
    ): Single<MarvelCharacters>

    companion object {
        operator fun invoke(
                connectivityInterceptor: ConnectivityInterceptor
        ): MarvelApiService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter(API_KEY_STRING, API_KEY)
                        .addQueryParameter(TIME_STAMP_STRING, TIME_STAMP)
                        .addQueryParameter(HASH_STRING, HASH)
                        .build()

                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .addInterceptor(connectivityInterceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MarvelApiService::class.java)
        }
    }
}