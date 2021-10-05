package com.kks.cleankotlintest.network.exception

import java.io.IOException

/**
 * Created by Ptut on 2019-09-10
 */
data class NetworkException constructor(
    var errorCode: Int = 0,
    var errorBody: String? = null
) : IOException()
