package com.velaris.mobile.core.util

import java.math.BigDecimal
import java.math.RoundingMode

fun formatNumber(value: BigDecimal): String = when {
    value >= BigDecimal("1000000000") ->
        "${value.divide(BigDecimal("1000000000")).setScale(1, RoundingMode.HALF_UP)}B"
    value >= BigDecimal("1000000") ->
        "${value.divide(BigDecimal("1000000")).setScale(1, RoundingMode.HALF_UP)}M"
    value >= BigDecimal("1000") ->
        "${value.divide(BigDecimal("1000")).setScale(1, RoundingMode.HALF_UP)}K"
    else -> value.toPlainString()
}