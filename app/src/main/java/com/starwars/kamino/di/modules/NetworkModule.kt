package com.starwars.kamino.di.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.starwars.kamino.BuildConfig
import com.starwars.kamino.base.KaminoApplication
import com.starwars.kamino.di.scopes.ApplicationScope
import com.starwars.kamino.ui.planet.api.PlanetServiceBase
import com.starwars.kamino.utils.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    companion object {
        private const val OKHTTP_TIMEOUT_SECONDS = 60L
        private const val BASE_URL = "https://private-anon-509bb59a02-starwars2.apiary-mock.com/"
    }

    @Provides
    fun provideNetworkConnectionInterceptor(context: Context) =
        NetworkConnectionInterceptor(context)

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(networkConnectionInterceptor: NetworkConnectionInterceptor): OkHttpClient {
        return with(OkHttpClient.Builder()) {
            connectTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(OKHTTP_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.BASIC
            }
            addInterceptor(logging)
            addInterceptor(networkConnectionInterceptor)
            build()
        }
    }

    @Provides
    @ApplicationScope
    fun provideRetrofitInstance(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideGlideInstance(
        application: KaminoApplication,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(application).setDefaultRequestOptions(requestOptions)
    }

    @ApplicationScope
    @Provides
    fun providePlanetServiceBase(retrofit: Retrofit): PlanetServiceBase =
        retrofit.create(PlanetServiceBase::class.java)

}