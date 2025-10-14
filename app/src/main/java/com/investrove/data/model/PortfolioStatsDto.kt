package com.investrove.data.model

data class PortfolioStatsDto(
    val totalValue: Double = 0.0,
    val totalGain: Double = 0.0,
    val avgReturnPercentage: Double = 0.0,
    val historicalData: List<ValuePoint> = emptyList(),
    val categoryDistribution: List<CategoryValue> = emptyList(),
    val investmentTypeDistribution: List<TypeValue> = emptyList(),
    val topPerformers: List<TopPerformer> = emptyList()
)

data class ValuePoint(
    val day: String,
    val totalValue: Double
)

data class CategoryValue(
    val category: String,
    val totalValue: Double
)

data class TypeValue(
    val investmentType: String,
    val totalValue: Double
)

data class TopPerformer(
    val id: Long,
    val name: String,
    val gain: Double
)
