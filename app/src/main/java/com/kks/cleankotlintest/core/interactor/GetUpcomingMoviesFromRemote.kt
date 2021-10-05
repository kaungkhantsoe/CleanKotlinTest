package com.kks.cleankotlintest.core.interactor

import com.kks.cleankotlintest.core.data.MovieRepository

class GetUpcomingMoviesFromRemote(private val movieRepository: MovieRepository) {
    operator fun invoke(page: Int = 1) = movieRepository.getUpComingMovieListForPage(page)
}