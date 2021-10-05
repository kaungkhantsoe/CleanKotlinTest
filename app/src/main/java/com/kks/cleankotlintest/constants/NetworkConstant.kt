package com.kks.cleankotlintest.constants

enum class NetworkConstant(val value: Int,val title: String) {
    NO_NETWORK_CONNECT(9, "No network connection"),
    SERVER_ERROR(7, "Server error"),
    UNAUTHORIZED(6, "Unauthorized"),
    BAD_REQUEST(5, "Bad Request"),
    UNKNOWN_ERROR(4, "Unkown error")
}