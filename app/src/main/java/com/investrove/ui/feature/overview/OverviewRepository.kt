package com.investrove.ui.feature.overview

import com.velaris.api.client.StatsApi
import com.velaris.api.client.model.StatsOverview
import javax.inject.Inject

class OverviewRepository @Inject constructor(
    private val statsApi: StatsApi
) {
    suspend fun getOverview(): StatsOverview? {
        val response = statsApi.getStatsOverview()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getCategoryStats() = statsApi.getStatsByCategory().body() ?: emptyList()
    suspend fun getTrendStats() = statsApi.getStatsTrend().body() ?: emptyList()
}
