package com.investrove.data.local

import androidx.room.Dao
import androidx.room.Query
import com.investrove.data.model.CategoryValue
import com.investrove.data.model.PerformanceMetricsDto
import com.investrove.data.model.TopPerformer
import com.investrove.data.model.ValuePoint
import com.investrove.data.model.TypeValue
import java.time.LocalDate

@Dao
interface InvestmentStatsDao {

    @Query("""
        SELECT SUM(currentValue) AS totalValue,
               SUM(currentValue - purchasePrice) AS totalGain,
               AVG((currentValue - purchasePrice) * 1.0 / purchasePrice) * 100 AS avgReturnPercentage
        FROM investment_items
        WHERE acquisitionDate BETWEEN :start AND :end
    """)
    suspend fun getPerformanceMetrics(start: LocalDate, end: LocalDate): PerformanceMetricsDto

    @Query("""
        SELECT date(acquisitionDate) AS day, SUM(currentValue) AS totalValue
        FROM investment_items
        WHERE acquisitionDate BETWEEN :start AND :end
        GROUP BY day
        ORDER BY day
    """)
    suspend fun getHistoricalData(start: LocalDate, end: LocalDate): List<ValuePoint>

    @Query("""
        SELECT category, SUM(currentValue) AS totalValue
        FROM investment_items
        WHERE acquisitionDate BETWEEN :start AND :end
        GROUP BY category
    """)
    suspend fun getCategoryDistribution(start: LocalDate, end: LocalDate): List<CategoryValue>

    @Query("""
        SELECT investmentType AS investmentType, SUM(currentValue) AS totalValue
        FROM investment_items
        WHERE acquisitionDate BETWEEN :start AND :end
        GROUP BY investmentType
    """)
    suspend fun getInvestmentTypeDistribution(start: LocalDate, end: LocalDate): List<TypeValue>

    @Query("""
        SELECT id, name, (currentValue - purchasePrice) AS gain
        FROM investment_items
        WHERE acquisitionDate BETWEEN :start AND :end
        ORDER BY gain DESC
        LIMIT :limit
    """)
    suspend fun getTopPerformers(start: LocalDate, end: LocalDate, limit: Int): List<TopPerformer>
}