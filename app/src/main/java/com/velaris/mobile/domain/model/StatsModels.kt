package com.velaris.mobile.domain.model

import com.velaris.api.client.model.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

data class CategoryStats(
    val categoryName: String,
    val itemCount: Int,
    val totalValue: BigDecimal,
    val uniqueAssets: Int,
    val percentageOfTotal: Double
)

data class TrendStats(
    val date: LocalDate,
    val value: BigDecimal,
    val itemsAdded: Int
)

data class OverviewStats(
    val totalAssets: Int,
    val totalItems: Int,
    val totalValue: BigDecimal,
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
    fun fromTrendItem(dto: TrendItem) = TrendStats(
        date = dto.date ?: LocalDate.MIN,
        value = dto.totalValue ?: BigDecimal.ZERO,
        itemsAdded = dto.itemsAdded ?: 0
    )

    fun fromTrendDiffItem(dto: TrendDiffItem) = TrendDiffStats(
        date = dto.date ?: OffsetDateTime.MIN,
        totalValue = dto.totalValue ?: BigDecimal.ZERO,
        deltaValue = dto.deltaValue ?: BigDecimal.ZERO,
        deltaPercent = dto.deltaPercent ?: 0.0
    )

    fun fromCategoryItem(dto: CategoryItem) = CategoryStats(
        categoryName = dto.category ?: "",
        itemCount = dto.itemCount ?: 0,
        totalValue = dto.totalValue ?: BigDecimal.ZERO,
        uniqueAssets = dto.uniqueAssets ?: 0,
        percentageOfTotal = dto.percentageOfTotal ?: 0.0
    )

    fun fromOverviewItem(dto: OverviewItem) = OverviewStats(
        totalAssets = dto.assetCount ?: 0,
        totalValue = dto.totalValue ?: BigDecimal.ZERO,
        totalItems = dto.totalItems ?: 0,
        currency = dto.currency ?: "USD"
    )

    fun fromTagItem(dto: TagItem) = TagStats(
        tag = dto.tag ?: "",
        assetsCount = dto.assetsCount ?: 0,
        totalValue = dto.totalValue ?: BigDecimal.ZERO
    )

    fun fromTopHoldingItem(dto: TopHoldingItem, assetId: Long = 0L, deltaValue: BigDecimal = BigDecimal.ZERO) = TopMoversStats(
        assetId = assetId,
        name = dto.name ?: "",
        category = dto.category,
        totalValue = dto.totalValue ?: BigDecimal.ZERO,
        deltaValue = deltaValue
    )

    fun fromCategoryTrendItem(dto: CategoryTrendItem) = CategoryTrendStats(
        category = dto.category ?: "",
        createdDate = dto.createdDate ?: OffsetDateTime.MIN,
        totalValue = dto.totalValue ?: BigDecimal.ZERO
    )
}