package com.velaris.mobile.core.util

import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafeApiCaller @Inject constructor(
    private val json: Json
) {

    suspend fun <T> call(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                when {
                    body != null -> ApiResult.Success(body)
                    response.code() == 204 -> ApiResult.Empty
                    else -> ApiResult.Error(response.code(), "Empty response body")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val parsedError = errorBody?.let {
                    runCatching {
                        json.decodeFromString(ApiErrorResponse.serializer(), it)
                    }.getOrNull()
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
            if (e is CancellationException) throw e
            val friendlyMessage = ErrorMapper.fromException(e)
            ApiResult.Error(null, friendlyMessage, e)
        }
    }
}