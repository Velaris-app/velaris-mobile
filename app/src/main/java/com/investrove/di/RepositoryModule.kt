package com.investrove.di

import com.investrove.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePortfolioStatsRepository(
            investmentRepository: InvestmentItemRepository
    ): PortfolioStatsRepository {
        return PortfolioStatsRepository(investmentRepository)
    }

    // ... existing code ...
}
