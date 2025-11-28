package com.example.nanithappybirthday.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nanithappybirthday.model.BirthdayData
import com.example.nanithappybirthday.ui.theme.NanitHappyBirthdayTheme
import com.example.nanithappybirthday.ui.theme.TextColor
import com.example.nanithappybirthday.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NanitHappyBirthdayTheme {
                val uiState = viewModel.uiState.collectAsState().value

                MainScreen(
                    ipAddress = uiState.ipAddress,
                    onIpAddressChange = viewModel::saveIpAddress,
                    birthdayData = uiState.birthdayData
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    birthdayData: BirthdayData?
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (birthdayData != null) {
                BirthdayScreen(birthdayData)
            } else {
                Text(
                    if (ipAddress.isEmpty()) "No IP address was set yet. Press the settings button to configure it." else "Oops! No birthday data was received.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(30.dp)
                )
            }

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp),
                )
            }
        }
    }

    if (showDialog) {
        // todo add validation!
        IpAddressDialog(
            currentIp = ipAddress,
            onDismiss = { showDialog = false },
            onConfirm = { newIp ->
                onIpAddressChange(newIp)
                showDialog = false
            }
        )
    }
}

@Composable
fun IpAddressDialog(
    currentIp: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var ipText by remember { mutableStateOf(currentIp) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter IP Address", style = MaterialTheme.typography.bodyMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = ipText,
                    onValueChange = { ipText = it },
                    label = { Text("IP Address") },
                    placeholder = { Text("192.168.1.115") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(ipText) }) {
                Text("Save & Request", style = MaterialTheme.typography.bodySmall)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}

@Preview
@Composable
fun ScreenPreviewNoIP() {
    NanitHappyBirthdayTheme {
        MainScreen("", {}, null)
    }
}

@Preview
@Composable
fun ScreenPreviewNoBirthdayData() {
    NanitHappyBirthdayTheme {
        MainScreen("127.0.0.1", {}, null)
    }
}

@Preview
@Composable
fun ScreenPreview() {
    NanitHappyBirthdayTheme {
        MainScreen("127.0.0.1", {}, BirthdayData("keren", 1760044800, "fox"))
    }
}

@Preview
@Composable
fun DialogPreview() {
    NanitHappyBirthdayTheme {
        IpAddressDialog("192.168.1.115", {}, {})
    }
}