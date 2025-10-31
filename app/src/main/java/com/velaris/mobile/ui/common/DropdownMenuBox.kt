package com.velaris.mobile.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchableDropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = if (expanded) query else selectedOption,
            onValueChange = { query = it },
            readOnly = !expanded,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        )

        val filteredOptions = if (query.isEmpty()) options else options.filter { it.contains(query, ignoreCase = true) }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                query = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                        query = ""
                    }
                )
            }
        }
    }
}
