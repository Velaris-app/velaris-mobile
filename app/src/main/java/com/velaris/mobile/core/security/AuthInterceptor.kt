package com.velaris.mobile.core.security

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
        val token = runBlocking { tokenProvider.getAccessToken() }
        val request = chain.request().newBuilder()

        if (!token.isNullOrEmpty() && !tokenProvider.isTokenExpired(token)) {
            request.header("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}
