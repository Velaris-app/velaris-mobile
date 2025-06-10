package com.investrove.data.repository

import com.investrove.data.local.InvestmentItemDao
import com.investrove.data.model.InvestmentItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestmentItemRepository @Inject constructor(
    private val dao: InvestmentItemDao
) {

    suspend fun getAllCollectibles(): List<InvestmentItem> = dao.getAllCollectibles()

    suspend fun insert(item: InvestmentItem) = dao.insert(item)

    suspend fun delete(item: InvestmentItem) = dao.delete(item)

    suspend fun update(item: InvestmentItem) = dao.update(item)
}
