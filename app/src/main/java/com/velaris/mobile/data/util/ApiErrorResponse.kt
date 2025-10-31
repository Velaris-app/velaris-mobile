package com.velaris.mobile.data.util

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val timestamp: String? = null,
    val status: Int? = null,
    val error: String? = null,
    val message: String? = null,
    val details: String? = null
)
