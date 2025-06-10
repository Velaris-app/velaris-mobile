package com.investrove.data.repository

import com.investrove.data.model.*
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PortfolioStatsRepository
@Inject
constructor(private val investmentRepository: InvestmentItemRepository) {
    suspend fun getPortfolioStats(dateRange: DateRange? = null): PortfolioStats {
        val items = investmentRepository.getAllCollectibles()
        if (items.isEmpty()) {
            return PortfolioStats()
        }

        val filteredItems =
                dateRange?.let { range ->
                    items.filter { item ->
                        item.acquisitionDate?.let { date ->
                            date.isAfter(range.start.minusDays(1)) &&
                                    date.isBefore(range.end.plusDays(1))
                        }
                                ?: false
                    }
                }
                        ?: items

        if (filteredItems.isEmpty()) {
            return PortfolioStats()
        }

        val totalValue = filteredItems.sumOf { it.currentValue ?: 0.0 }
        val totalPurchaseValue = filteredItems.sumOf { it.purchasePrice ?: 0.0 }
        val totalReturn =
                if (totalPurchaseValue > 0) {
                    ((totalValue - totalPurchaseValue) / totalPurchaseValue) * 100
                } else 0.0

        val categoryStats = calculateCategoryStats(filteredItems)
        val investmentTypeDistribution = calculateInvestmentTypeDistribution(filteredItems)
        val historicalData = generateHistoricalData(filteredItems)
        val (topPerformers, worstPerformers) = calculatePerformers(filteredItems)

        return PortfolioStats(
                totalValue = totalValue,
                totalPurchaseValue = totalPurchaseValue,
                totalReturn = totalReturn,
                monthlyReturn = totalReturn / 12, // Simplified calculation
                categoryDistribution = categoryStats,
                investmentTypeDistribution = investmentTypeDistribution,
                historicalData = historicalData,
                topPerformers = topPerformers,
                worstPerformers = worstPerformers
        )
    }

    private fun calculateCategoryStats(items: List<InvestmentItem>): Map<String, CategoryStats> {
        return items.groupBy { it.category }.mapValues { (_, categoryItems) ->
            val totalValue = categoryItems.sumOf { it.currentValue ?: 0.0 }
            val totalPurchaseValue = categoryItems.sumOf { it.purchasePrice ?: 0.0 }
            val returnPercentage =
                    if (totalPurchaseValue > 0) {
                        ((totalValue - totalPurchaseValue) / totalPurchaseValue) * 100
                    } else 0.0

            CategoryStats(
                    totalValue = totalValue,
                    totalPurchaseValue = totalPurchaseValue,
                    returnPercentage = returnPercentage,
                    itemCount = categoryItems.size
            )
        }
    }

    private fun calculateInvestmentTypeDistribution(
            items: List<InvestmentItem>
    ): InvestmentTypeDistribution {
        val alternativeCategories =
                setOf("Złoto", "Kryptowaluty", "Figurka", "LEGO", "Karta Kolekcjonerska")
        val traditionalCategories = setOf("Mieszkanie", "Akcje", "Fundusz ETF", "Waluta")

        val alternativeValue =
                items.filter { it.category in alternativeCategories }.sumOf {
                    it.currentValue ?: 0.0
                }

        val traditionalValue =
                items.filter { it.category in traditionalCategories }.sumOf {
                    it.currentValue ?: 0.0
                }

        return InvestmentTypeDistribution(
                alternative = alternativeValue,
                traditional = traditionalValue
        )
    }

    private fun generateHistoricalData(items: List<InvestmentItem>): List<HistoricalDataPoint> {
        // This would be replaced with real historical data in a production app
        return List(30) { i ->
            val date = LocalDate.now().minusDays(29L - i)
            val value = items.sumOf { it.currentValue ?: 0.0 } * (1 + (i * 0.01))
            HistoricalDataPoint(date, value)
        }
    }

    private fun calculatePerformers(
            items: List<InvestmentItem>
    ): Pair<List<InvestmentPerformance>, List<InvestmentPerformance>> {
        val performances =
                items
                        .filter { it.purchasePrice != null && it.currentValue != null }
                        .map { item ->
                            val returnPercentage =
                                    ((item.currentValue!! - item.purchasePrice!!) /
                                            item.purchasePrice!!) * 100
                            InvestmentPerformance(
                                    id = item.id,
                                    name = item.name,
                                    category = item.category,
                                    purchaseValue = item.purchasePrice!!,
                                    currentValue = item.currentValue!!,
                                    returnPercentage = returnPercentage
                            )
                        }
                        .sortedByDescending { it.returnPercentage }

        return Pair(performances.take(5), performances.takeLast(5).reversed())
    }
}

data class DateRange(val start: LocalDate, val end: LocalDate)
