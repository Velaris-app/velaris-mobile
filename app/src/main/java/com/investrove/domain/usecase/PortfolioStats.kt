package com.investrove.domain.usecase

import com.investrove.data.repository.InvestmentItemRepository
import javax.inject.Inject

data class PortfolioStats(
    val totalItems: Int,
    val totalEstimatedValue: Double,
    val averageEmotionalValue: Double
)

class GetPortfolioStatsUseCase @Inject constructor(
    private val repository: InvestmentItemRepository
) {
    suspend operator fun invoke(): PortfolioStats {
        val items = repository.getAllCollectibles()
        val total = items.size
        val valueSum = items.sumOf { it.currentValue ?: 0.0 }
        val avgEmotion = if (total > 0) items.map { it.emotionalValue }.average() else 0.0

        return PortfolioStats(
            totalItems = total,
            totalEstimatedValue = valueSum,
            averageEmotionalValue = avgEmotion
        )
    }
}
