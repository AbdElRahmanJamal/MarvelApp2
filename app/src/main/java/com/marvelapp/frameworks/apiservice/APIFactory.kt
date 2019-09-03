package com.marvelapp.frameworks.apiservice


import com.google.gson.GsonBuilder
import com.marvelapp.frameworks.apiservice.interceptor.ConnectivityInterceptor
import com.marvelapp.marvelcharacterdetails.data.MarvelDetailsPageApis
import com.marvelapp.marvelcharacterhome.data.MarvelHomePageApis
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val apiFactory = module {

    val keyConnectivityInterceptor = "ConnectivityInterceptor"
    val keyHeadersInterceptor = "HeadersInterceptor"
    val keyRetrofitOkHttpClient = "RetrofitOkHttpClient"

    single<Interceptor>(keyConnectivityInterceptor) { ConnectivityInterceptor(androidApplication()) }

    single(keyHeadersInterceptor) {
        Interceptor { chain ->
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
    }

    single(keyRetrofitOkHttpClient) {
        OkHttpClient.Builder()
            .addInterceptor(get(keyConnectivityInterceptor))
            .addInterceptor(get(keyHeadersInterceptor))
            .build()
    }

    single { GsonBuilder().serializeNulls().create() }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get(keyRetrofitOkHttpClient))
            .build()
    }

    factory { get<Retrofit>().create(MarvelHomePageApis::class.java) }
    factory { get<Retrofit>().create(MarvelDetailsPageApis::class.java) }
}