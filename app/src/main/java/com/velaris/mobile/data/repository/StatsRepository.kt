package com.velaris.mobile.data.repository

import com.velaris.api.client.StatsApi
import com.velaris.api.client.model.CategoryTrendRequest
import com.velaris.api.client.model.SearchFilter
import com.velaris.api.client.model.TrendRequest
import com.velaris.mobile.core.util.ApiResult
import com.velaris.mobile.core.util.SafeApiCaller
import com.velaris.mobile.core.util.mapSuccess
import com.velaris.mobile.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val api: StatsApi,
    private val safeApiCaller: SafeApiCaller
) {
    suspend fun getCategoryStats(): ApiResult<List<CategoryStats>> =
        safeApiCaller.call {
            api.getStatsByCategory()
        }.mapSuccess { list ->
            list.map { StatsMapper.fromCategoryItem(it) }
        }

    suspend fun getTrendStats(trendRequest: TrendRequest): ApiResult<List<TrendStats>> =
        safeApiCaller.call {
            api.getStatsTrend(trendRequest)
        }.mapSuccess { list ->
            list.map { StatsMapper.fromTrendItem(it) }
        }

    suspend fun getOverviewStats(): ApiResult<OverviewStats> =
        safeApiCaller.call {
            api.getStatsOverview()
        }.mapSuccess { item ->
            StatsMapper.fromOverviewItem(item)
        }

    suspend fun getTagStats(): ApiResult<List<TagStats>> =
        safeApiCaller.call {
            api.getStatsTag()
        }.mapSuccess { list ->
            list.map { StatsMapper.fromTagItem(it) }
        }

    suspend fun getTrendDiffStats(): ApiResult<List<TrendDiffStats>> =
        safeApiCaller.call {
            api.getTrendDiffStats()
        }.mapSuccess { list ->
            list.map { StatsMapper.fromTrendDiffItem(it) }
        }

    suspend fun getTopMovers(searchFilter: SearchFilter): ApiResult<List<TopMoversStats>> =
        safeApiCaller.call {
            api.getTopHoldings(searchFilter)
        }.mapSuccess { list ->
            list.map { StatsMapper.fromTopHoldingItem(it) }
        }

    suspend fun getCategoryTrend(categoryTrendRequest: CategoryTrendRequest): ApiResult<List<CategoryTrendStats>> =
        safeApiCaller.call {
            api.getCategoryTrend(categoryTrendRequest)
        }.mapSuccess { list ->
            list.map { StatsMapper.fromCategoryTrendItem(it) }
        }
}