package com.investrove.di

import com.investrove.data.local.InvestmentItemDao
import com.investrove.data.local.InvestmentStatsDao
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
    fun provideItemRepository(dao: InvestmentItemDao):
            InvestmentItemRepository = InvestmentItemRepository(dao)

    @Provides
    @Singleton
    fun provideItemStatsRepository(dao: InvestmentStatsDao):
            PortfolioStatsRepository = PortfolioStatsRepository(dao)
}
