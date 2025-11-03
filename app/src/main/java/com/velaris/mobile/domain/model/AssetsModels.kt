package com.velaris.mobile.domain.model

import com.velaris.api.client.model.*
import java.math.BigDecimal
import java.time.OffsetDateTime

data class AssetItem(
    val id: Long?,
    val name: String,
    val category: String,
    val description: String?,
    val purchasePrice: BigDecimal,
    val currency: String,
    val condition: String?,
    val year: Int?,
    val quantity: Int,
    val images: List<String>,
    val tags: List<String>
)

data class RecentActivity(
    val assetId: Long,
    val changeDate: OffsetDateTime,
    val changedFields: Map<String, Any>? = null,
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
    tags = tags ?: emptyList()
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
    tags = tags
)

fun RecentActivitiesItem.toDomain(): RecentActivity = RecentActivity(
    assetId = assetId?.toLong() ?: 0L,
    changeDate = changeDate,
    changedFields = changedFields,
    activityType = when (activityType) {
        ActivityType.CREATED -> ActivityTypeEnum.CREATED
        ActivityType.UPDATED -> ActivityTypeEnum.UPDATED
        ActivityType.DELETED -> ActivityTypeEnum.DELETED
    }
)