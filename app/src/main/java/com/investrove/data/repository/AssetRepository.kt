package com.investrove.data.repository

import com.velaris.api.client.AssetsApi
import com.velaris.api.client.model.Asset
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Response

@Singleton
class AssetRepository @Inject constructor(
    private val api: AssetsApi
) {

    suspend fun listAssets(): Response<List<Asset>> = api.listAssets()

    suspend fun addAsset(asset: Asset): Response<Asset> = api.addAsset(asset)

    suspend fun deleteAsset(id: String): Response<Unit> = api.deleteAsset(id)

    suspend fun getAsset(id: String): Response<Asset> = api.getAsset(id)

    suspend fun updateAsset(id: String, asset: Asset): Response<Asset> =
        api.modifyAsset(id, asset)
}
