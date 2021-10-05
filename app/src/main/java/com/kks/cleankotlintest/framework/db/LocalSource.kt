package com.kks.cleankotlintest.framework.db

import com.kks.cleankotlintest.core.data.LocalDataSource
import com.kks.cleankotlintest.presentation.model.MovieVO
import com.kks.cleankotlintest.presentation.model.toDomainModel
import com.kks.cleankotlintest.presentation.model.toPresentationModel
import com.kks.cleankotlintest.core.domain.MovieRequest as DomainMovie

class LocalSource(
    private val db: AppDb
): LocalDataSource
{
    override fun getMovieListFor(page: Int): List<DomainMovie> =
        db.MovieDao().getMoviesFrom(page).map(MovieVO::toDomainModel)


    override fun getMovieWith(id: Int): DomainMovie? =
        db.MovieDao().getMovieWith(id)?.toDomainModel()


    override fun insertMovieList(list: List<DomainMovie>) =
        db.MovieDao().insertMovies(list.map(DomainMovie::toPresentationModel))

}