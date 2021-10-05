package com.kks.cleankotlintest.core.interactor

data class Interactors(
    val getMovie: GetMovie,
    val getMovieFromLocal: GetMoviesFromLocal,
    val getMoviesFromRemote: GetMoviesFromRemote,
    val insertMovie: InsertMovie
)