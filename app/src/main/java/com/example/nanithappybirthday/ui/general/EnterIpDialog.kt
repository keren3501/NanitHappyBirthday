package com.example.nanithappybirthday.ui.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun TextFieldDialog(
    title: String,
    textFieldLabel: String = "",
    placeHolder: String = "",
    currValue: String,
    confirmText: String,
    onConfirm: (String) -> Unit,
    dismissText: String,
    onDismiss: () -> Unit,
) {
    var valueText by remember { mutableStateOf(currValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, style = MaterialTheme.typography.bodyMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = valueText,
                    onValueChange = { valueText = it },
                    label = { Text(textFieldLabel) },
                    placeholder = { Text(placeHolder) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(valueText) }) {
                Text(confirmText, style = MaterialTheme.typography.bodySmall)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText, style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}