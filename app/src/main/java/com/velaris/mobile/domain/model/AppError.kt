package com.velaris.mobile.domain.model

sealed class AppError(open val message: String? = null) {

    data class Network(override val message: String? = "No internet connection") : AppError(message)
    data class Timeout(override val message: String? = "Server timeout") : AppError(message)
    data class Unauthorized(override val message: String? = "Unauthorized") : AppError(message)
    data class Server(val code: Int, override val message: String? = "Server error") : AppError(message)
    data class NotFound(override val message: String? = "Resource not found") : AppError(message)
    data class Unknown(override val message: String? = "Unexpected error") : AppError(message)

    companion object {
        fun fromException(e: Throwable): AppError = when (e) {
            is java.net.UnknownHostException -> Network()
            is java.net.SocketTimeoutException -> Timeout()
            is retrofit2.HttpException -> when (e.code()) {
                401 -> Unauthorized()
                404 -> NotFound()
                else -> Server(e.code())
            }
            else -> Unknown(e.message)
        }
    }
}
