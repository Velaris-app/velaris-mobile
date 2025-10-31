package com.velaris.mobile.data.util

object ErrorMapper {

    fun toUserMessage(code: Int?, backendMessage: String?): String {
        return when (code) {
            400 -> backendMessage ?: "Invalid data — please check your input."
            401 -> "Your session has expired. Please log in again."
            403 -> "You don't have permission to perform this action."
            404 -> "The requested resource was not found."
            408 -> "Request timed out. Please try again."
            409 -> backendMessage ?: "Data conflict. Please try again."
            422 -> backendMessage ?: "Invalid request data. Please verify your input."
            429 -> "Too many requests. Please wait a moment and try again."
            500 -> "Server error. Please try again later."
            502, 503, 504 -> "The service is temporarily unavailable. Please try again later."
            else -> backendMessage ?: "An unexpected error occurred. Please try again."
        }
    }

    fun fromException(e: Exception): String {
        return when (e) {
            is java.net.UnknownHostException -> "No internet connection. Please check your network."
            is java.net.SocketTimeoutException -> "Connection timed out. Please try again."
            is java.net.ConnectException -> "Unable to connect to the server. Please try again."
            else -> e.localizedMessage ?: "An unexpected error occurred."
        }
    }
}
