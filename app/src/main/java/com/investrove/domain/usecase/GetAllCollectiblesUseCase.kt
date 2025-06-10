package com.investrove.domain.usecase

import com.investrove.data.model.InvestmentItem
import com.investrove.data.repository.InvestmentItemRepository
import javax.inject.Inject

class GetAllCollectiblesUseCase @Inject constructor(
    private val repository: InvestmentItemRepository
) {
    suspend operator fun invoke(): List<InvestmentItem> {
        return repository.getAllCollectibles()
    }
}
