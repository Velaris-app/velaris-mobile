//package com.investrove.ui.feature.insights.components.cards
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import com.investrove.data.model.*
//import com.investrove.ui.common.SectionCard
//import kotlin.math.roundToInt
//
//@Composable
//fun InvestmentTypeComparison(
//    investmentTypeDistribution: List<TypeValue>,
//    modifier: Modifier = Modifier
//) {
//    SectionCard(
//        title = "Porównanie typów inwestycji",
//        modifier = modifier
//    ) {
//            val alternativeValue = investmentTypeDistribution
//                    .filter { it.type == "Alternative" }
//                .sumOf { it.totalValue }
//
//            val traditionalValue = investmentTypeDistribution
//                .filter { it.type == "Traditional" }
//                .sumOf { it.totalValue }
//
//            val total = alternativeValue + traditionalValue
//
//            if (total > 0) {
//                val alternativePercentage = (alternativeValue / total * 100).roundToInt()
//                val traditionalPercentage = (traditionalValue / total * 100).roundToInt()
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text("Alternatywne")
//                    Text(
//                        "$alternativePercentage%",
//                        style = MaterialTheme.typography.titleLarge,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                }
//                Column {
//                    Text("Tradycyjne")
//                    Text(
//                        "$traditionalPercentage%",
//                        style = MaterialTheme.typography.titleLarge,
//                        color = MaterialTheme.colorScheme.secondary
//                    )
//                }
//            }
//        } else {
//            Text("Brak danych")
//        }
//    }
//}