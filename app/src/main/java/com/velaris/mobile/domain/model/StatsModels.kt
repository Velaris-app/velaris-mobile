package com.velaris.mobile.domain.model

import com.velaris.api.client.model.*
import java.math.BigDecimal
import java.time.OffsetDateTime

data class CategoryStats(
    val categoryName: String,       // DTO: CategoryItem.category
    val itemCount: Int,             // DTO: CategoryItem.itemCount
    val totalValue: BigDecimal,     // DTO: CategoryItem.totalValue
    val uniqueAssets: Int,          // DTO: CategoryItem.uniqueAssets
    val percentageOfTotal: Double   // DTO: CategoryItem.percentageOfTotal
)

data class TrendStats(
    val date: OffsetDateTime,
    val value: BigDecimal,
    val itemsAdded: Int
)

data class OverviewStats(
    val totalAssets: Int,           // DTO: OverviewItem.assetCount
    val totalItems: Int,            // DTO: OverviewItem.totalItems
    val totalValue: BigDecimal,     // DTO: OverviewItem.totalValue
    val currency: String            // DTO: OverviewItem.currency
)


data class TagStats(
    val tag: String,                // DTO: TagItem.tag
    val assetsCount: Int,           // DTO: TagItem.assetsCount
    val totalValue: BigDecimal      // DTO: TagItem.totalValue
)


data class TrendDiffStats(
    val date: OffsetDateTime,       // DTO: TrendDiffItem.date
    val totalValue: BigDecimal,     // DTO: TrendDiffItem.totalValue
    val deltaValue: BigDecimal,     // DTO: TrendDiffItem.deltaValue
    val deltaPercent: Double        // DTO: TrendDiffItem.deltaPercent
)


data class TopMoversStats(
    val assetId: Long,              // opcjonalnie, jeśli API zwraca id
    val name: String,               // DTO: TopHoldingItem.name
    val category: String?,          // DTO: TopHoldingItem.category
    val totalValue: BigDecimal,     // DTO: TopHoldingItem.totalValue
    val deltaValue: BigDecimal      // dodatkowe pole np. różnica wartości
)


data class CategoryTrendStats(
    val category: String,           // DTO: CategoryTrendItem.category
    val createdDate: OffsetDateTime,// DTO: CategoryTrendItem.createdDate
    val totalValue: BigDecimal      // DTO: CategoryTrendItem.totalValue
)


object StatsMapper {

    fun fromTrendItem(dto: TrendItem) = TrendStats(
        date = dto.date ?: OffsetDateTime.MIN,
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