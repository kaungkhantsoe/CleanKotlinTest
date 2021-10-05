package com.kks.cleankotlintest.core.interactor

import com.kks.cleankotlintest.core.data.MovieRepository

class GetMoviesFromLocal(private val movieRepository: MovieRepository) {
    operator fun invoke(page: Int = 1, type: Int) =
        movieRepository.getLocalMovieListForPage(page, type)
}