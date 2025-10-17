package com.velaris.mobile.data.repository

import com.velaris.api.client.StatsApi
import com.velaris.mobile.domain.model.CategoryStats
import com.velaris.mobile.domain.model.CategoryTrendStats
import com.velaris.mobile.domain.model.OverviewStats
import com.velaris.mobile.domain.model.StatsMapper
import com.velaris.mobile.domain.model.TagStats
import com.velaris.mobile.domain.model.TopMoversStats
import com.velaris.mobile.domain.model.TrendDiffStats
import com.velaris.mobile.domain.model.TrendStats
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val api: StatsApi
) {

    suspend fun getCategoryStats(): List<CategoryStats> =
        api.getStatsByCategory().body()?.map(StatsMapper::fromDto) ?: emptyList()

    suspend fun getTrendStats(): List<TrendStats> =
        api.getStatsTrend().body()?.map(StatsMapper::fromDto) ?: emptyList()

    suspend fun getOverviewStats(): OverviewStats =
        api.getStatsOverview().body()?.let(StatsMapper::fromDto)
            ?: OverviewStats(0, BigDecimal.ZERO, 0, "PLN")

    suspend fun getTagStats(): List<TagStats> =
        api.getStatsTag().body()?.map(StatsMapper::fromDto) ?: emptyList()

    suspend fun getTrendDiffStats(): List<TrendDiffStats> =
        api.getTrendDiffStats().body()?.map(StatsMapper::fromDto) ?: emptyList()

    suspend fun getTopMovers(): List<TopMoversStats> =
        api.getTopMovers().body()?.map(StatsMapper::fromDto) ?: emptyList()

    suspend fun getCategoryTrend(category: String): List<CategoryTrendStats> =
        api.getCategoryTrend(category).body()?.map(StatsMapper::fromDto) ?: emptyList()
}