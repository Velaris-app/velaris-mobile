package com.velaris.mobile.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun VelarisSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) { data ->
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp)),
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            action = {
                data.visuals.actionLabel?.let {
                    TextButton(onClick = { data.performAction() }) {
                        Text(it)
                    }
                }
            }
        ) { Text(data.visuals.message) }
    }
}

@Composable
fun InlineError(
    message: String,
    onRetry: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium)
        onRetry?.let {
            Spacer(Modifier.height(16.dp))
            Button(onClick = it) { Text("Retry") }
        }
    }
}

@Composable
fun EmptyState(
    title: String,
    message: String,
    onAddClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Inbox, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(message, style = MaterialTheme.typography.bodySmall)
        onAddClick?.let {
            Spacer(Modifier.height(16.dp))
            Button(onClick = it) { Text("Add") }
        }
    }
}