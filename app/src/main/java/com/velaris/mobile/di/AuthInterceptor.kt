package com.velaris.mobile.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.velaris.api.client.AuthApi
import com.velaris.api.client.model.GrantType
import com.velaris.api.client.model.TokenRequest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

class AuthInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenProvider.getAccessToken() }
        val requestBuilder = chain.request().newBuilder()

        if (!token.isNullOrEmpty() && !tokenProvider.isTokenExpired(token)) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401 || response.code == 403) {
            runBlocking {
                val newToken = refreshToken(tokenProvider.getRefreshToken())
                if (newToken != null) {
                    tokenProvider.saveRefreshToken(newToken)
                } else {
                    tokenProvider.clearTokens()
                }
            }
        }

        return response
    }

    private fun refreshToken(refreshToken: String?): String? {
        if (refreshToken.isNullOrEmpty()) return null
        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://cc4733e17d8b.ngrok-free.app/")
            .client(client)
            .addConverterFactory(Json { ignoreUnknownKeys = true }
                .asConverterFactory("application/json".toMediaType()))
            .build()
        val authApi = retrofit.create(AuthApi::class.java)

        val response = runBlocking { authApi.getToken(
            TokenRequest(grantType = GrantType.REFRESH_TOKEN, refreshToken = refreshToken))
        }
        return if (response.isSuccessful) response.body()?.accessToken else null
    }
}
