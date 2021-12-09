package com.kks.cleankotlintest.framework.remote

import com.kks.cleankotlintest.BuildConfig
import com.kks.cleankotlintest.core.data.RemoteDataSource
import com.kks.cleankotlintest.core.domain.MovieListRequest
import com.kks.cleankotlintest.extensions.executeOrThrow
import com.kks.cleankotlintest.network.ApiInterface

class RemoteSource(private val apiInterface: ApiInterface) : RemoteDataSource {
    override fun requestMovieListForUpComing(page: Int): MovieListRequest? =
        apiInterface.getMoviesUpComing(BuildConfig.API_KEY, "en-US", page).executeOrThrow()

    override fun requestMovieListForPopular(page: Int): MovieListRequest? =
        apiInterface.getMoviesPopular(BuildConfig.API_KEY, "en-US", page).executeOrThrow()

}