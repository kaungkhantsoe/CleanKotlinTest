package com.kks.cleankotlintest.core.data

import com.kks.cleankotlintest.core.domain.MovieListRequest

interface RemoteDataSource {
    fun requestMovieListFor(page: Int): MovieListRequest
}