package com.velaris.mobile.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenProvider.getToken() }

        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrEmpty() && !tokenProvider.isTokenExpired(token)) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 403 || response.code == 401) {
            CoroutineScope(Dispatchers.IO).launch {
                tokenProvider.clearTokens()
            }
        }

        return response
    }
}
