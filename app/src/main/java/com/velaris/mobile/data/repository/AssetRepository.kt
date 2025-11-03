package com.velaris.mobile.data.repository

import com.velaris.api.client.AssetsApi
import com.velaris.mobile.data.util.ApiResult
import com.velaris.mobile.data.util.mapSuccess
import com.velaris.mobile.data.util.safeApiCall
import com.velaris.mobile.domain.model.AssetItem
import com.velaris.mobile.domain.model.RecentActivity
import com.velaris.mobile.domain.model.toApi
import com.velaris.mobile.domain.model.toDomain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepository @Inject constructor(
    private val api: AssetsApi
) {

    suspend fun getAssets(): ApiResult<List<AssetItem>> =
        safeApiCall { api.listAssets() }
            .mapSuccess { list ->
                list.map { it.toDomain() }
            }

    suspend fun getAsset(id: Long): ApiResult<AssetItem> =
        safeApiCall { api.getAsset(id) }
            .mapSuccess { it.toDomain() }

    suspend fun addAsset(asset: AssetItem): ApiResult<AssetItem> =
        safeApiCall { api.addAsset(asset.toApi()) }
            .mapSuccess { it.toDomain() }

    suspend fun updateAsset(id: Long, asset: AssetItem): ApiResult<AssetItem> =
        safeApiCall { api.modifyAsset(id, asset.toApi()) }
            .mapSuccess { it.toDomain() }

    suspend fun deleteAsset(id: String): ApiResult<Unit> =
        safeApiCall { api.deleteAsset(id) }
            .mapSuccess { }

    suspend fun getRecentActivities(): ApiResult<List<RecentActivity>> =
        safeApiCall { api.listAssetActivities() }
            .mapSuccess { list ->
                list.map { it.toDomain() }
            }
}