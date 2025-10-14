package com.investrove.di

import com.velaris.api.client.AssetsApi
import com.velaris.api.client.AuthApi
import com.velaris.api.client.StatsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.modules.SerializersModule
import java.math.BigDecimal
import java.time.OffsetDateTime

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: TokenProvider): AuthInterceptor =
        AuthInterceptor(tokenProvider)

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            serializersModule = SerializersModule {
                contextual(BigDecimal::class, BigDecimalSerializer)
                contextual(OffsetDateTime::class, OffsetDateTimeSerializer)
            }
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl("https://ef3eefdc06a8.ngrok-free.app/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAssetsApi(retrofit: Retrofit): AssetsApi =
        retrofit.create(AssetsApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideStatsApi(retrofit: Retrofit): StatsApi =
        retrofit.create(StatsApi::class.java)
}
