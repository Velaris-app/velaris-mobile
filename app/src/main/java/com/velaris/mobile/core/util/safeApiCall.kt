package com.velaris.mobile.core.util

import kotlinx.serialization.json.Json
import retrofit2.Response

private val jsonParser = Json { ignoreUnknownKeys = true }

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                ApiResult.Success(body)
            } else {
                ApiResult.Error(response.code(), "Empty response body")
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val parsedError = errorBody?.let {
                runCatching { jsonParser.decodeFromString(ApiErrorResponse.serializer(), it) }.getOrNull()
            }

            val friendlyMessage = ErrorMapper.toUserMessage(
                code = parsedError?.status ?: response.code(),
                backendMessage = parsedError?.message ?: parsedError?.error
            )

            ApiResult.Error(
                code = parsedError?.status ?: response.code(),
                message = friendlyMessage
            )
        }
    } catch (e: Exception) {
        val friendlyMessage = ErrorMapper.fromException(e)
        ApiResult.Error(null, friendlyMessage, e)
    }
}