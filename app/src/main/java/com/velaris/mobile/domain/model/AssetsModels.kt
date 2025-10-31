package com.velaris.mobile.domain.model

import com.velaris.api.client.model.*
import java.math.BigDecimal
import java.time.OffsetDateTime

data class AssetItem(
    val id: Long,
    val name: String,
    val category: String,
    val description: String?,
    val purchasePrice: BigDecimal,
    val currency: String,
    val condition: String?,
    val year: Int?,
    val quantity: Int,
    val images: List<String>,
    val tags: List<String>,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?
)

data class RecentActivity(
    val assetId: Long,
    val name: String,
    val category: String?,
    val purchasePrice: BigDecimal,
    val quantity: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val activityType: ActivityTypeEnum
)

enum class ActivityTypeEnum {
    CREATED, UPDATED, DELETED
}

// Mapper DTO -> Domain

fun Asset.toDomain(): AssetItem = AssetItem(
    id = id ?: 0L,
    name = name ?: "",
    category = category ?: "",
    description = description,
    purchasePrice = purchasePrice ?: BigDecimal.ZERO,
    currency = currency ?: "USD",
    condition = condition,
    year = year,
    quantity = quantity ?: 1,
    images = images ?: emptyList(),
    tags = tags ?: emptyList(),
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun AssetItem.toApi(): Asset = Asset(
    id = id,
    name = name,
    category = category,
    description = description,
    purchasePrice = purchasePrice,
    currency = currency,
    condition = condition,
    year = year,
    quantity = quantity,
    images = images,
    tags = tags,
    createdAt = createdAt ?: OffsetDateTime.now(),
    updatedAt = updatedAt ?: OffsetDateTime.now()
)

fun RecentActivitiesItem.toDomain(): RecentActivity = RecentActivity(
    assetId = assetId?.toLong() ?: 0L,
    name = "",
    category = null,
    purchasePrice = BigDecimal.ZERO,
    quantity = 0,
    createdAt = changeDate,
    updatedAt = changeDate,
    activityType = when (activityType) {
        ActivityType.CREATED -> ActivityTypeEnum.CREATED
        ActivityType.UPDATED -> ActivityTypeEnum.UPDATED
        ActivityType.DELETED -> ActivityTypeEnum.DELETED
        else -> ActivityTypeEnum.UPDATED
    }
)