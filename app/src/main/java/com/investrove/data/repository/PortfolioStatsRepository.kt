package com.investrove.data.repository

import com.investrove.data.local.InvestmentStatsDao
import com.investrove.data.model.*
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PortfolioStatsRepository @Inject constructor(
    private val dao: InvestmentStatsDao
) {
    suspend fun getPortfolioStats(start: LocalDate, end: LocalDate): PortfolioStatsDto {
        val metrics = dao.getPerformanceMetrics(start, end)
        return PortfolioStatsDto(
            totalValue = metrics.totalValue,
            totalGain = metrics.totalGain,
            avgReturnPercentage = metrics.avgReturnPercentage,
            historicalData = dao.getHistoricalData(start, end),
            categoryDistribution = dao.getCategoryDistribution(start, end),
            investmentTypeDistribution = dao.getInvestmentTypeDistribution(start, end),
            topPerformers = dao.getTopPerformers(start, end, limit = 5)
        )
    }
}

data class DateRange(val start: LocalDate, val end: LocalDate)
