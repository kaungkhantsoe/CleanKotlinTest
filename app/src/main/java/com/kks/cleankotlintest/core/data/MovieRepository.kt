package com.kks.cleankotlintest.core.data

import com.kks.cleankotlintest.core.domain.MovieRequest

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localSource: LocalDataSource
) {
    fun getRemoteMovieListForPage(page: Int) = remoteDataSource.requestMovieListFor(page)
    fun getLocalMovieListForPage(page: Int) = localSource.getMovieListFor(page)
    fun getMovie(id: Int) = localSource.getMovieWith(id)
    fun insertMovies(list: List<MovieRequest>) = localSource.insertMovieList(list)
}