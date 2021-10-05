package com.kks.cleankotlintest.presentation.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kks.cleankotlintest.common.Pageable
import com.kks.cleankotlintest.core.domain.MovieRequest as DomainMovie

@Entity(tableName = "movie")
data class MovieVO(
    @PrimaryKey
    val id: Int,

    val originalTitle: String,

    val posterPath: String,

    val overview: String,

    var pageNumber: Int? = 1,

    var isPLiked: Int,

    var isULiked: Int,

    var ptype: Int = 0,

    var utype: Int = 0,

    ) : Pageable

fun DomainMovie.toPresentationModel(): MovieVO = MovieVO(
    id,
    original_title ?: "",
    poster_path ?: "",
    overview ?: "",
    isPLiked = isPLiked,
    isULiked = isULiked,
    ptype = ptype,
    utype = utype
)

fun MovieVO.toDomainModel(): DomainMovie = DomainMovie(
    id, originalTitle, posterPath, overview, isPLiked = isPLiked, ptype = ptype, isULiked = isULiked,utype = utype
)

