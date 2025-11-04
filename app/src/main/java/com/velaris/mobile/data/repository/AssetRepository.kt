package com.velaris.mobile.data.repository

import com.velaris.api.client.AssetsApi
import com.velaris.mobile.core.util.ApiResult
import com.velaris.mobile.core.util.SafeApiCaller
import com.velaris.mobile.core.util.mapSuccess
import com.velaris.mobile.domain.model.AssetItem
import com.velaris.mobile.domain.model.RecentActivity
import com.velaris.mobile.domain.model.toApi
import com.velaris.mobile.domain.model.toDomain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepository @Inject constructor(
    private val api: AssetsApi,
    private val safeApiCaller: SafeApiCaller
) {
    suspend fun getAssets(): ApiResult<List<AssetItem>> =
        safeApiCaller.call { api.listAssets() }
            .mapSuccess { list ->
                list.map { it.toDomain() }
            }

    suspend fun getAsset(id: Long): ApiResult<AssetItem> =
        safeApiCaller.call { api.getAsset(id) }
            .mapSuccess { it.toDomain() }

    suspend fun addAsset(asset: AssetItem): ApiResult<AssetItem> =
        safeApiCaller.call { api.addAsset(asset.toApi()) }
            .mapSuccess { it.toDomain() }

    suspend fun updateAsset(id: Long, asset: AssetItem): ApiResult<AssetItem> =
        safeApiCaller.call { api.modifyAsset(id, asset.toApi()) }
            .mapSuccess { it.toDomain() }

    suspend fun deleteAsset(id: Long): ApiResult<Unit> =
        safeApiCaller.call { api.deleteAsset(id) }
            .mapSuccess { }

    suspend fun getRecentActivities(): ApiResult<List<RecentActivity>> =
        safeApiCaller.call { api.listAssetActivities() }
            .mapSuccess { list ->
                list.map { it.toDomain() }
            }
}