package com.velaris.mobile.core.security

import com.velaris.api.client.AuthApi
import com.velaris.api.client.model.GrantType
import com.velaris.api.client.model.TokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenProvider: TokenProvider,
   private val authApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.header("Authorization") != null) return null

        val refreshToken = runBlocking { tokenProvider.getRefreshToken() } ?: return null

        val newAccessToken = runBlocking {
            val result = authApi.getToken(
                TokenRequest(grantType = GrantType.REFRESH_TOKEN, refreshToken = refreshToken)
            )
            result.body()?.accessToken
        } ?: return null

        runBlocking { tokenProvider.saveAccessToken(newAccessToken) }

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }
}
