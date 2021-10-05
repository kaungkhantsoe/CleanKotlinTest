package com.kks.cleankotlintest.network

import com.kks.cleankotlintest.core.domain.MovieListRequest
import com.kks.cleankotlintest.presentation.model.MovieListVO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/now_playing")
    fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieListRequest>
}