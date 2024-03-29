package com.kks.cleankotlintest.core.data

import com.kks.cleankotlintest.core.domain.MovieRequest

interface LocalDataSource {
    fun getMovieListFor(page: Int, type: Int): List<MovieRequest>
    fun getMovieWith(id: Int): MovieRequest?
    fun insertMovieList(list: List<MovieRequest>)
    fun changeLikeMovie(movieRequest: MovieRequest): Int
}