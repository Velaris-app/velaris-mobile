package com.velaris.mobile.ui.feature.overview.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velaris.mobile.domain.model.CategoryStats
import com.velaris.mobile.ui.common.LabeledProgressBar
import com.velaris.mobile.ui.common.SectionCard
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun InvestmentCategoriesCard(
    categories: List<CategoryStats>,
    modifier: Modifier = Modifier
) {
    SectionCard(title = "Investment Categories", modifier = modifier) {
        val total = categories.fold(BigDecimal.ZERO) { acc, item -> acc + item.totalValue }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val percentage = if (total > BigDecimal.ZERO) {
                    category.totalValue.multiply(BigDecimal(100))
                        .divide(total, 1, RoundingMode.HALF_UP)
                        .toFloat()
                } else 0f

                LabeledProgressBar(
                    label = category.categoryName,
                    progress = percentage / 100,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}