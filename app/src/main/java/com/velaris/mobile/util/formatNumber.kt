package com.velaris.mobile.util

import java.math.BigDecimal

fun formatNumber(value: BigDecimal): String = when {
    value >= BigDecimal(1_000_000) -> "${(value.divide(BigDecimal(1_000_000))).setScale(1, java.math.RoundingMode.HALF_UP)}M"
    value >= BigDecimal(1_000) -> "${(value.divide(BigDecimal(1_000))).setScale(1, java.math.RoundingMode.HALF_UP)}K"
    else -> value.toPlainString()
}