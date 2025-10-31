package com.velaris.mobile.ui.feature.insights.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.velaris.mobile.domain.model.CategoryTrendStats
import com.velaris.mobile.util.formatNumber
import java.math.BigDecimal

@Composable
fun CategoryTrendCard(
    categoryTrend: List<CategoryTrendStats>,
    modifier: Modifier = Modifier
) {
    val maxValue = categoryTrend.maxOfOrNull { it.totalValue } ?: BigDecimal.ONE

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Category Trend",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (categoryTrend.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No category trend data",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            } else {
                categoryTrend.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Data label
                        Text(
                            text = item.createdDate.toLocalDate().toString(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.width(100.dp)
                        )

                        // Mini-bar proportional to max value
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth((item.totalValue.toFloat() / maxValue.toFloat()))
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Value label
                        Text(
                            text = formatNumber(item.totalValue) + " PLN",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    if (index != categoryTrend.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}