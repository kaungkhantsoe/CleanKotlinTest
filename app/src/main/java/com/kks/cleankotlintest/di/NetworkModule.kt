package com.kks.cleankotlintest.di

import android.content.Context
import com.kks.cleankotlintest.BuildConfig
import com.kks.cleankotlintest.core.data.MovieRepository
import com.kks.cleankotlintest.core.interactor.*
import com.kks.cleankotlintest.framework.db.AppDb
import com.kks.cleankotlintest.framework.db.LocalSource
import com.kks.cleankotlintest.framework.remote.RemoteSource
import com.kks.cleankotlintest.network.ApiInterface
import com.kks.cleankotlintest.util.NetworkListener
import com.kks.cleankotlintest.util.NetworkUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideNetworkUtil(androidContext()) }
    single { provideAppDb(androidContext()) }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideLoggingInterceptor() }
    single { provideApiService(get()) }
    single { provideRemoteSource(get()) }
    single { provideLocalSource(get()) }
    single { provideMovieRepository(get(),get()) }
    single { provideInteractors(get()) }
}

fun provideNetworkUtil(context: Context): NetworkListener {
    return NetworkUtil(context)
}

fun provideAppDb(context: Context): AppDb {
    return AppDb.getDatabase(context)
}

fun provideRemoteSource(api: ApiInterface): RemoteSource {
    return RemoteSource(api)
}

fun provideLocalSource(appDb: AppDb): LocalSource {
    return LocalSource(appDb)
}

fun provideMovieRepository(remoteSource: RemoteSource, localSource: LocalSource): MovieRepository {
    return MovieRepository(remoteSource, localSource)
}

fun provideInteractors(movieRepository: MovieRepository): Interactors {
    return Interactors(
        GetMovie(movieRepository),
        GetMoviesFromLocal(movieRepository),
        GetMoviesFromRemote(movieRepository),
        InsertMovie(movieRepository),
        ChangeLikeMovie(movieRepository)
    )
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
): OkHttpClient =
    OkHttpClient().newBuilder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()


fun provideLoggingInterceptor(): HttpLoggingInterceptor =if (BuildConfig.DEBUG)
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    else
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

fun provideApiService(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)
