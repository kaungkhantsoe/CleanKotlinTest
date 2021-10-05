package com.kks.cleankotlintest.core.interactor

import com.kks.cleankotlintest.core.data.MovieRepository

class GetPopularMoviesFromRemote (private val movieRepository: MovieRepository) {
    operator fun invoke(page: Int = 1) = movieRepository.getPopularMovieListForPage(page)
}