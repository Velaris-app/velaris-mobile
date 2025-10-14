package com.investrove.di

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenProvider.getToken() }

        val requestBuilder = chain.request().newBuilder()
        if (!token.isNullOrEmpty() && !tokenProvider.isTokenExpired(token)) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            runBlocking { tokenProvider.clearTokens() }
        }

        return response
    }
}
