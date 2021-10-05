package com.kks.cleankotlintest.network

import com.kks.cleankotlintest.core.domain.MovieListRequest
import com.kks.cleankotlintest.presentation.model.MovieListVO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/upcoming")
    fun getMoviesUpComing(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieListRequest>

    @GET("movie/popular")
    fun getMoviesPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieListRequest>
}