package com.kks.cleankotlintest.extensions

import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import java.net.UnknownHostException

fun <T> Call<T>.executeOrThrow(): T? {
//    val response = this.execute()
//    if (response.isSuccessful.not()) {
//        val res = response.errorBody()!!
//        val json = JSONObject(String(res.bytes()))
//        val errMsg = json.getString("message")
//        throw NetworkException(response.code())
//    }
//
//    return response.body()
    var response: Response<T>? = null
    try {
        response = this.execute()
        if (response.isSuccessful.not()) {
            val res = response.errorBody()?.let {
//                val json = JSONObject(String(it.bytes()))
//                val errMsg = json.getString("message")
//                val errCode = json.getString("code")
                val errMsg = response.message()
                val errCode = response.code()
                if (errCode in 401..500)
                    throw HttpException(response)
                else throw Exception()
            }

        }
    } catch (httpException: HttpException) {
        throw httpException
    } catch (unknownHostException: UnknownHostException) {
        throw unknownHostException
    } catch (e: Exception) {
        throw e
    }
    return response.body()

}