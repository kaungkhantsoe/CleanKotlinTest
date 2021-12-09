package com.kks.cleankotlintest.core.data

import com.kks.cleankotlintest.core.domain.MovieListRequest

interface RemoteDataSource {
    fun requestMovieListForUpComing(page: Int): MovieListRequest?
    fun requestMovieListForPopular(page: Int): MovieListRequest?
}