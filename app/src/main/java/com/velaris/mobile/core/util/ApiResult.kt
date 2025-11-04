package com.velaris.mobile.core.util

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val code: Int?,
        val message: String?,
        val exception: Throwable? = null
    ) : ApiResult<Nothing>()
    data object Empty : ApiResult<Nothing>()
}

inline fun <T, R> ApiResult<T>.mapSuccess(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Success -> ApiResult.Success(transform(data))
        is ApiResult.Error -> this
        is ApiResult.Empty -> ApiResult.Empty
    }
}

fun <T> ApiResult<T>.dataOrNull(): T? = when (this) {
    is ApiResult.Success -> this.data
    else -> null
}

fun <T> ApiResult<List<T>>.dataOrEmpty(): List<T> = when (this) {
    is ApiResult.Success -> this.data
    is ApiResult.Empty -> emptyList()
    else -> emptyList()
}