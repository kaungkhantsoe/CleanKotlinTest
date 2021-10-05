package com.kks.cleankotlintest.core.domain

/**
 * Created by kaungkhantsoe on 5/18/21.
 **/
data class MovieRequest(
    val id: Int,
    val original_title: String?,
    val poster_path: String?,
    val overview: String?,
    var isPLiked: Int = 0,
    var isULiked: Int = 0,
    var ptype: Int = 0,
    var utype: Int = 0,
)