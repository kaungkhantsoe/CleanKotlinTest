package com.kks.cleankotlintest.framework.db.dao

import androidx.room.*
import com.kks.cleankotlintest.presentation.model.MovieVO

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movieVO: MovieVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movieVOS: List<MovieVO>)

    @Query("SELECT * FROM movie WHERE id = :id ")
    fun getMovieWith(id: Int): MovieVO?

    @Query("SELECT * FROM movie WHERE pageNumber = :pageNumber AND ptype = :type")
    fun getPopularMoviesFrom(pageNumber: Int, type: Int): List<MovieVO>

    @Query("SELECT * FROM movie WHERE pageNumber = :pageNumber AND utype = :type")
    fun getUpcomingMoviesFrom(pageNumber: Int, type: Int): List<MovieVO>

    @Query("DELETE FROM movie")
    fun deleteMovies()

    @Update
    fun changeLikedMovie(movieVO: MovieVO): Int
}