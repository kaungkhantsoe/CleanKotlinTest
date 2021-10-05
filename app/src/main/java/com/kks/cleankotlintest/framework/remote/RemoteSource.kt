package com.kks.cleankotlintest.framework.remote

import com.kks.cleankotlintest.BuildConfig
import com.kks.cleankotlintest.core.data.RemoteDataSource
import com.kks.cleankotlintest.core.domain.MovieListRequest
import com.kks.cleankotlintest.extensions.executeOrThrow
import com.kks.cleankotlintest.network.ApiInterface
import com.kks.cleankotlintest.presentation.model.toDomainModel

class RemoteSource(private val apiInterface: ApiInterface) : RemoteDataSource {
    override fun requestMovieListFor(page: Int): MovieListRequest =
        apiInterface.getMovies(BuildConfig.API_KEY, "en-US", page).executeOrThrow()
}