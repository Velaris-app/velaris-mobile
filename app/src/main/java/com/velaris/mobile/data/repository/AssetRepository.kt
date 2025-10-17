package com.velaris.mobile.data.repository

import com.velaris.mobile.domain.model.AssetItem
import com.velaris.mobile.domain.model.RecentActivity
import com.velaris.mobile.domain.model.toApi
import com.velaris.mobile.domain.model.toDomain
import com.velaris.api.client.AssetsApi
import com.velaris.api.client.model.Asset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssetRepository @Inject constructor(
    private val api: AssetsApi
) {

    suspend fun getAssets(): List<Asset> {
        val response = api.listAssets()
        if (!response.isSuccessful) throw Exception("Failed to load assets: ${response.code()}")
        return response.body()?.map { it.toDomain() } ?: emptyList()
    }

    suspend fun getAsset(id: String): Asset {
        val response = api.getAsset(id)
        if (!response.isSuccessful) throw Exception("Failed to load asset: ${response.code()}")
        return response.body()?.toDomain() ?: throw Exception("Asset not found")
    }

    suspend fun addAsset(asset: AssetItem): Asset {
        val response = api.addAsset(asset.toApi())
        if (!response.isSuccessful) throw Exception("Failed to add asset: ${response.code()}")
        return response.body()?.toDomain() ?: throw Exception("Invalid response")
    }

    suspend fun updateAsset(id: String, asset: AssetItem): Asset {
        val response = api.modifyAsset(id, asset.toApi())
        if (!response.isSuccessful) throw Exception("Failed to update asset: ${response.code()}")
        return response.body()?.toDomain() ?: throw Exception("Invalid response")
    }

    suspend fun deleteAsset(id: String) {
        val response = api.deleteAsset(id)
        if (!response.isSuccessful) throw Exception("Failed to delete asset: ${response.code()}")
    }

    suspend fun getRecentActivities(): List<RecentActivity> {
        val response = api.listAssetActivities()
        if (!response.isSuccessful) throw Exception("Failed to load activities: ${response.code()}")
        return response.body()?.map { it.toDomain() } ?: emptyList()
    }
}
