package com.kks.cleankotlintest.common

/**
 * Created by kaungkhantsoe on 18/05/2021.
 **/
sealed class DataState<out T> {
//    class Success<T>(val data: Any): DataState<T>()
    class Error<T>(val error: String) : DataState<T>()
    object EndReach : DataState<Nothing>()

    data class Success<out T>(val data: T): DataState<T>()
    data class GenericError(val code: Int? = null, val error: String? = null): DataState<Nothing>()
    object NetworkError: DataState<Nothing>()
}