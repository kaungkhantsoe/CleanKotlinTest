package com.kks.cleankotlintest.core.interactor

import com.kks.cleankotlintest.core.data.MovieRepository
import com.kks.cleankotlintest.core.domain.MovieRequest

class InsertMovie(private val movieRepository: MovieRepository) {
    operator fun invoke(list: List<MovieRequest>) =
        movieRepository.insertMovies(list)
}