package com.kks.cleankotlintest.callback

interface MainListener {
    fun onClickMovie(id: Int, position: Int)
    fun onClickLike(id: Int, position: Int)
}