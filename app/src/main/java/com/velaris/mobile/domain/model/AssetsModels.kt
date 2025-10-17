package com.velaris.mobile.domain.model

import com.velaris.api.client.model.ActivityType
import com.velaris.api.client.model.Asset
import com.velaris.api.client.model.RecentActivitiesItem
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
    val assetId: Int,
    val name: String,
    val category: String,
    val purchasePrice: BigDecimal,
    val quantity: Int,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val activityType: ActivityTypeEnum
)

enum class ActivityTypeEnum {
    CREATED, UPDATED
}

fun Asset.toDomain(): Asset = Asset(
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
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun RecentActivitiesItem.toDomain(): RecentActivity = RecentActivity(
    assetId = assetId ?: 0,
    name = name ?: "",
    category = category ?: "",
    purchasePrice = purchasePrice ?: BigDecimal.ZERO,
    quantity = quantity ?: 0,
    createdAt = createdAt ?: OffsetDateTime.now(),
    updatedAt = updatedAt ?: OffsetDateTime.now(),
    activityType = when (activityType) {
        ActivityType.CREATED -> ActivityTypeEnum.CREATED
        ActivityType.UPDATED -> ActivityTypeEnum.UPDATED
        else -> ActivityTypeEnum.UPDATED
    }
)