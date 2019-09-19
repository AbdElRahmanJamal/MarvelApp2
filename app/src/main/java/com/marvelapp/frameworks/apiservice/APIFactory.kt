package com.marvelapp.frameworks.apiservice


import android.content.Context
import com.google.gson.GsonBuilder
import com.marvelapp.BuildConfig.API_KEY
import com.marvelapp.BuildConfig.HASH
import com.marvelapp.marvelcharacterdetails.data.MarvelDetailsPageApis
import com.marvelapp.marvelcharacterhome.data.MarvelHomePageApis
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


val apiFactory = module {

    val keyHeadersInterceptor = "HeadersInterceptor"
    val keyRetrofitOkHttpClient = "RetrofitOkHttpClient"



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
        createCachedClient(androidApplication())
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

private fun createCachedClient(context: Context): OkHttpClient.Builder {
    val httpCacheDirectory = File(context.cacheDir, "cache_file")

    val cache = Cache(httpCacheDirectory, (30 * 1024 * 1024).toLong())
    val okHttpClient = OkHttpClient().newBuilder()
    okHttpClient.cache(cache)
    okHttpClient.networkInterceptors().add(
        Interceptor { chain ->
            val maxAge = 300 // read from cache for 1 minute
            val originalRequest = chain.request()
            val cacheHeaderValue = if (isOnline(context))
                "public, max-age=$maxAge"
            else {
                val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                "public, only-if-cached, max-stale=$maxStale"
            }
            val request = originalRequest.newBuilder().build()
            val response = chain.proceed(request)
            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheHeaderValue)
                .build()
        }
    )

    return okHttpClient
}
