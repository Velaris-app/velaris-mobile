package com.investrove.data.local// data/local/CollectibleDao.kt

import androidx.room.*
import com.investrove.data.model.InvestmentItem

@Dao
interface InvestmentItemDao {

    @Query("SELECT * FROM investment_items ORDER BY name ASC")
    suspend fun getAllCollectibles(): List<InvestmentItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InvestmentItem)

    @Delete
    suspend fun delete(item: InvestmentItem)

    @Update
    suspend fun update(item: InvestmentItem)
}