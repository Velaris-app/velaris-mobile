# Velaris Mobile API Documentation

This document provides guidance for using Velaris Mobile APIs. It covers **repositories**, **data models**, **API calls**, **error handling**, and **example usage**.

---

## 1. Overview

Velaris Mobile interacts with backend services via repositories located in `com.velaris.mobile.data.repository`.

* **StatsRepository**: fetches overview, category, and trend statistics.
* **AssetRepository**: fetches assets and recent activity.

All API calls return `ApiResult<T>` for safe handling.

---

## 2. ApiResult

Located in `com.velaris.mobile.core.util.ApiResult`:

```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int?, val message: String?) : ApiResult<Nothing>()
}
```

### Usage:

* Use `dataOrNull()` to get the data or null if an error.
* Use `dataOrEmpty()` for lists to avoid null checks.

```kotlin
val result: ApiResult<List<AssetItem>> = assetRepository.getAssets()
val assets = result.dataOrEmpty()
```

---

## 3. StatsRepository

### Functions:

```kotlin
suspend fun getOverviewStats(): ApiResult<OverviewStats>
suspend fun getCategoryStats(): ApiResult<List<CategoryStats>>
suspend fun getTrendStats(request: TrendRequest): ApiResult<List<TrendData>>
```

* **OverviewStats** includes `totalAssets`, `totalItems`, `totalValue`, `currency`.
* **CategoryStats** represents asset categories and their totals.
* **TrendData** represents historical performance.

### Example:

```kotlin
val overview = statsRepository.getOverviewStats().dataOrNull()
println("Total portfolio value: ${overview?.totalValue}")
```

---

## 4. AssetRepository

### Functions:

```kotlin
suspend fun getAssets(): ApiResult<List<AssetItem>>
suspend fun getAssetById(id: Long): ApiResult<AssetItem>
suspend fun addAsset(asset: AssetItem): ApiResult<AssetItem>
suspend fun updateAsset(asset: AssetItem): ApiResult<AssetItem>
suspend fun deleteAsset(id: Long): ApiResult<Unit>
suspend fun getRecentActivities(): ApiResult<List<RecentActivity>>
```

### Example:

```kotlin
val recent = assetRepository.getRecentActivities().dataOrEmpty()
recent.forEach { println("Asset #${it.assetId} changed") }
```

---

## 5. Error Handling

Use `ErrorMapper` to map API errors to user-friendly messages:

```kotlin
val errorMsg = ErrorMapper.toUserMessage(error.code, error.message)
```

---

## 6. Data Models

**OverviewStats**

```kotlin
data class OverviewStats(
    val totalAssets: Int,
    val totalItems: Int,
    val totalValue: BigDecimal,
    val currency: String
)
```

**AssetItem**

```kotlin
data class AssetItem(
    val id: Long?,
    val name: String,
    val category: String,
    val quantity: Int,
    val price: BigDecimal
)
```

**RecentActivity**

```kotlin
data class RecentActivity(
    val assetId: Long,
    val changedFields: Map<String, Any>?,
    val changeDate: OffsetDateTime,
    val activityType: ActivityTypeEnum
)
```

---

## 7. Best Practices

1. Always use `dataOrNull()` / `dataOrEmpty()` to avoid null checks.
2. Wrap API calls in `viewModelScope.launch {}` for asynchronous calls.
3. Use error messages from `ErrorMapper` to display in UI.
4. Avoid direct calls from Composables; use ViewModel state.

---

## 8. Example ViewModel Usage

```kotlin
class OverviewViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OverviewState())
    val state: StateFlow<OverviewState> = _state.asStateFlow()

    fun loadOverview() {
        viewModelScope.launch {
            val result = statsRepository.getOverviewStats()
            val overview = result.dataOrNull()
            _state.value = _state.value.copy(totalValue = overview?.totalValue ?: BigDecimal.ZERO)
        }
    }
}
```

---

This document should serve as a starting point for developers integrating with Velaris Mobile APIs.