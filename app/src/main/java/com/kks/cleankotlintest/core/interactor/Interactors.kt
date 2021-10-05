package com.kks.cleankotlintest.core.interactor

data class Interactors(
    val getMovie: GetMovie,
    val getMovieFromLocal: GetMoviesFromLocal,
    val getUpcomingMoviesFromRemote: GetUpcomingMoviesFromRemote,
    val insertMovie: InsertMovie,
    val changeLikeMovie: ChangeLikeMovie,
    val getPopularMoviesFromRemote: GetPopularMoviesFromRemote
)