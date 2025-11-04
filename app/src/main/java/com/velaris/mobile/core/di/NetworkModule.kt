package com.velaris.mobile.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.velaris.api.client.AssetsApi
import com.velaris.api.client.AuthApi
import com.velaris.api.client.StatsApi
import com.velaris.mobile.core.security.AuthInterceptor
import com.velaris.mobile.core.security.TokenAuthenticator
import com.velaris.mobile.core.serialization.BigDecimalSerializer
import com.velaris.mobile.core.serialization.LocalDateSerializer
import com.velaris.mobile.core.serialization.OffsetDateTimeSerializer
import com.velaris.mobile.core.serialization.UUIDSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://6e1728ca3bca.ngrok-free.app/" // TODO: move to BuildConfig

    // --- Qualifiers ---
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthenticatedClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PlainClient

    // --- JSON setup ---
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        serializersModule = SerializersModule {
            contextual(BigDecimal::class, BigDecimalSerializer)
            contextual(OffsetDateTime::class, OffsetDateTimeSerializer)
            contextual(UUID::class, UUIDSerializer)
            contextual(LocalDate::class, LocalDateSerializer)
        }
        ignoreUnknownKeys = true
        isLenient = true
    }

    // --- OkHttp Clients ---
    @Provides
    @Singleton
    @PlainClient
    fun providePlainOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAuthenticatedOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()

    // --- Retrofit Instances ---
    @Provides
    @Singleton
    @PlainClient
    fun providePlainRetrofit(
        @PlainClient okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAuthenticatedRetrofit(
        @AuthenticatedClient okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    // --- APIs ---
    @Provides
    @Singleton
    fun provideDefaultAuthApi(@PlainClient retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideAssetsApi(@AuthenticatedClient retrofit: Retrofit): AssetsApi =
        retrofit.create(AssetsApi::class.java)

    @Provides
    @Singleton
    fun provideStatsApi(@AuthenticatedClient retrofit: Retrofit): StatsApi =
        retrofit.create(StatsApi::class.java)
}