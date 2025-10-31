package com.velaris.mobile.data.util

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val code: Int?,
        val message: String?,
        val exception: Throwable? = null
    ) : ApiResult<Nothing>()
}

inline fun <T, R> ApiResult<T>.mapSuccess(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(data))
        is ApiResult.Error -> this
    }
}