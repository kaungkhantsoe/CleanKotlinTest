package com.kks.cleankotlintest.extensions

import com.kks.cleankotlintest.common.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */
//fun <T> safeApiCall(
//    apiCall: () -> T?
//): DataState<T?> {
//
//        try {
//            return apiCall()?.let { DataState.Success(it) } ?: DataState.Error(NetworkConstant.UNKNOWN_ERROR)
//
//        } catch (throwable: Throwable) {
//            throwable.printStackTrace()
//            when (throwable) {
//                is UnknownHostException -> return DataState.Error(NetworkConstant.NO_NETWORK_CONNECT)
//                is IOException -> return DataState.Error(NetworkConstant.SERVER_ERROR)
//                is TimeoutCancellationException -> return DataState.Error(NetworkConstant.SERVER_ERROR)
//                is HttpException -> {
//                    when (throwable.code()) {
//                        HTTP_BAD_REQUEST -> return DataState.Error(NetworkConstant.BAD_REQUEST)
//                        HTTP_INTERNAL_ERROR -> return DataState.Error(NetworkConstant.UNAUTHORIZED)
//                        HTTP_UNAUTHORIZED,HTTP_FORBIDDEN -> return DataState.Error(NetworkConstant.UNAUTHORIZED)
//                        HTTP_NOT_FOUND -> return DataState.Error(NetworkConstant.SERVER_ERROR)
//                        else -> return DataState.Error(NetworkConstant.SERVER_ERROR)
//                    }
//                }
//                else -> return DataState.Error(NetworkConstant.UNKNOWN_ERROR)
//            }
//        }
//
//}

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): DataState<T> {
    return withContext(dispatcher) {
        try {
            withTimeout(10000) {
                DataState.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> DataState.NetworkError
                is UnknownHostException -> DataState.NetworkError
                is TimeoutCancellationException -> DataState.NetworkError
                is HttpException -> {
                    val code = throwable.code()
//                    val errorResponse = convertErrorBody(throwable)
                    val errorMsg = throwable.message()
                    DataState.GenericError(code, errorMsg)
                }
                else -> {
                    DataState.GenericError(null, null)
                }
            }
        }
    }
}

//private fun convertErrorBody(throwable: HttpException) {
//    return try {
//        throwable.response()?.errorBody()?.source()?.let {
//            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
//            moshiAdapter.fromJson(it)
//        }
//    } catch (exception: Exception) {
//        null
//    }
//}