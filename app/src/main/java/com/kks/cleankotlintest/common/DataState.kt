package com.kks.cleankotlintest.common

import com.kks.cleankotlintest.constants.NetworkConstant

/**
 * Created by kaungkhantsoe on 18/05/2021.
 **/
sealed class DataState<out T> {
    class Success<T>(val data: Any): DataState<T>()
    class Error<T>(val error: NetworkConstant) : DataState<T>()
    object EndReach : DataState<Nothing>()
}