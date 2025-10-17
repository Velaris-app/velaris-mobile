package com.velaris.mobile.domain.model

import com.velaris.api.client.model.*
import java.math.BigDecimal
import java.time.OffsetDateTime

data class CategoryStats(
    val categoryName: String,
    val itemCount: Int,
    val totalValue: BigDecimal,
    val uniqueAssets: Int,
    val percentageOfTotal: Double
)

data class TrendStats(
    val date: OffsetDateTime,
    val value: BigDecimal,
    val itemsAdded: Int
)

data class OverviewStats(
    val totalAssets: Int,
    val totalValue: BigDecimal,
    val totalItems: Int,
    val currency: String
)

data class TagStats(
    val tag: String,
    val assetsCount: Int,
    val totalValue: BigDecimal
)

data class TrendDiffStats(
    val date: OffsetDateTime,
    val totalValue: BigDecimal,
    val deltaValue: BigDecimal,
    val deltaPercent: Double
)

data class TopMoversStats(
    val assetId: Long,
    val name: String,
    val category: String?,
    val totalValue: BigDecimal,
    val deltaValue: BigDecimal
)

data class CategoryTrendStats(
    val category: String,
    val createdDate: OffsetDateTime,
    val totalValue: BigDecimal
)

object StatsMapper {

    fun fromDto(dto: StatsCategoryItem): CategoryStats =
        CategoryStats(
            categoryName = dto.category ?: "",
            itemCount = dto.itemCount ?: 0,
            totalValue = dto.totalValue ?: BigDecimal.ZERO,
            uniqueAssets = dto.uniqueAssets ?: 0,
            percentageOfTotal = dto.percentageOfTotal ?: 0.0
        )

    fun fromDto(dto: StatsTrendItem): TrendStats =
        TrendStats(
            date = dto.date ?: OffsetDateTime.now(),
            value = dto.totalValue ?: BigDecimal.ZERO,
            itemsAdded = dto.itemsAdded ?: 0
        )

    fun fromDto(dto: StatsOverview): OverviewStats =
        OverviewStats(
            totalAssets = dto.totalItems ?: 0,
            totalValue = dto.totalValue ?: BigDecimal.ZERO,
            currency = dto.currency ?: "PLN",
            totalItems = dto.assetCount ?: 0
        )

    fun fromDto(dto: StatsTagItem): TagStats =
        TagStats(
            tag = dto.tag ?: "",
            assetsCount = dto.assetsCount ?: 0,
            totalValue = dto.totalValue ?: BigDecimal.ZERO
        )

    fun fromDto(dto: StatsTrendDiffItem): TrendDiffStats =
        TrendDiffStats(
            date = dto.date ?: OffsetDateTime.now(),
            totalValue = dto.totalValue ?: BigDecimal.ZERO,
            deltaValue = dto.deltaValue ?: BigDecimal.ZERO,
            deltaPercent = dto.deltaPercent ?: 0.0
        )

    fun fromDto(dto: StatsTopMoversItem): TopMoversStats =
        TopMoversStats(
            assetId = dto.assetId ?: 0L,
            name = dto.name ?: "",
            category = dto.category,
            totalValue = dto.totalValue ?: BigDecimal.ZERO,
            deltaValue = dto.deltaValue ?: BigDecimal.ZERO
        )

    fun fromDto(dto: StatsCategoryTrendItem): CategoryTrendStats =
        CategoryTrendStats(
            category = dto.category ?: "",
            createdDate = dto.createdDate ?: OffsetDateTime.now(),
            totalValue = dto.totalValue ?: BigDecimal.ZERO
        )
}