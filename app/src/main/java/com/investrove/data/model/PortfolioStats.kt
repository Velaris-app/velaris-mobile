package com.investrove.data.model

import java.time.LocalDate

data class PortfolioStats(
    val totalValue: Double = 0.0,
    val totalPurchaseValue: Double = 0.0,
    val totalReturn: Double = 0.0,
    val monthlyReturn: Double = 0.0,
    val categoryDistribution: Map<String, CategoryStats> = emptyMap(),
    val investmentTypeDistribution: InvestmentTypeDistribution = InvestmentTypeDistribution(),
    val historicalData: List<HistoricalDataPoint> = emptyList(),
    val topPerformers: List<InvestmentPerformance> = emptyList(),
    val worstPerformers: List<InvestmentPerformance> = emptyList()
)

data class CategoryStats(
    val totalValue: Double,
    val totalPurchaseValue: Double,
    val returnPercentage: Double,
    val itemCount: Int
)

data class InvestmentTypeDistribution(
    val alternative: Double = 0.0,
    val traditional: Double = 0.0
)

data class HistoricalDataPoint(
    val date: LocalDate,
    val value: Double
)

data class InvestmentPerformance(
    val id: Long,
    val name: String,
    val category: String,
    val purchaseValue: Double,
    val currentValue: Double,
    val returnPercentage: Double
) 