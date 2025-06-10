package com.investrove.ui.common.dropdown

import androidx.compose.ui.Modifier

data class DropdownConfig(
    val label: String,
    val isError: Boolean = false,
    val supportingText: String? = null,
    val errorMessage: String? = null,
    val enabled: Boolean = true,
    val modifier: Modifier = Modifier.Companion
)