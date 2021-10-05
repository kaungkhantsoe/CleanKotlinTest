package com.kks.cleankotlintest.core.data

import com.kks.cleankotlintest.core.domain.MovieRequest

class MovieRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localSource: LocalDataSource
) {
    fun getUpComingMovieListForPage(page: Int) = remoteDataSource.requestMovieListForUpComing(page)
    fun getPopularMovieListForPage(page: Int) = remoteDataSource.requestMovieListForPopular(page)
    fun getLocalMovieListForPage(page: Int, type: Int) = localSource.getMovieListFor(page, type)
    fun getMovie(id: Int) = localSource.getMovieWith(id)
    fun insertMovies(list: List<MovieRequest>) = localSource.insertMovieList(list)
    fun changeLikeMovie(movieRequest: MovieRequest): Int = localSource.changeLikeMovie(movieRequest)
}