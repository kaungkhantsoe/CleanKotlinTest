package com.kks.cleankotlintest.core.interactor

import com.kks.cleankotlintest.core.data.MovieRepository

class GetMovie(private val movieRepository: MovieRepository) {
    operator fun invoke(id: Int) = movieRepository.getMovie(id)
}