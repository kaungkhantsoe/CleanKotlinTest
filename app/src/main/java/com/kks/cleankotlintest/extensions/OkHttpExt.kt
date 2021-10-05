package com.kks.cleankotlintest.extensions

import com.kks.cleankotlintest.network.exception.NetworkException
import retrofit2.Call

fun <T> Call<T>.executeOrThrow(): T {
    val response = this.execute()
    if (response.isSuccessful.not()) {
        throw NetworkException(response.code())
    }

    return response.body() ?: throw NetworkException()
}