package com.kks.cleankotlintest.core.data

import com.kks.cleankotlintest.core.domain.MovieRequest

interface LocalDataSource {
    fun getMovieListFor(page: Int): List<MovieRequest>
    fun getMovieWith(id: Int): MovieRequest?
    fun insertMovieList(list: List<MovieRequest>)
}